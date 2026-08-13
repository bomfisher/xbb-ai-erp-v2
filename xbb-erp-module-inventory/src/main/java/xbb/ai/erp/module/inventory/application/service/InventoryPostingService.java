package xbb.ai.erp.module.inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.inventory.contract.InboundCommand;
import xbb.ai.erp.module.inventory.contract.InboundLine;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.OutboundCommand;
import xbb.ai.erp.module.inventory.contract.OutboundLine;
import xbb.ai.erp.module.inventory.contract.PostingResult;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationContext;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationResult;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationStrategy;
import xbb.ai.erp.module.inventory.domain.cost.MovingWeightedAverageCostStrategy;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.model.StockCostTransaction;
import xbb.ai.erp.module.inventory.domain.model.StockTransaction;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockCostTransactionRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InventoryPostingService implements InventoryCommandApi {
    private final StockBalanceRepository balanceRepository;
    private final StockTransactionRepository transactionRepository;
    private final StockCostTransactionRepository costTransactionRepository;
    private final PlatformTransactionManager transactionManager;
    private final CostCalculationStrategy costStrategy = new MovingWeightedAverageCostStrategy();

    @Override
    public PostingResult postInbound(InboundCommand command) {
        validate(command);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return postInboundOnce(command);
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, requiredDefinition());
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                return transactionTemplate.execute(status -> postInboundOnce(command));
            } catch (InventoryConcurrentException | DataIntegrityViolationException exception) {
            }
        }
        throw new BizException("库存并发繁忙，请稍后使用相同幂等键重试");
    }

    /**
     * 执行出库记账。出库成本由库存模块使用当前生效成本策略计算，调用方不参与成本计算。
     */
    @Override
    public PostingResult postOutbound(OutboundCommand command) {
        validate(command);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return postOutboundOnce(command);
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, requiredDefinition());
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                return transactionTemplate.execute(status -> postOutboundOnce(command));
            } catch (InventoryConcurrentException exception) {
            }
        }
        throw new BizException("库存并发繁忙，请稍后使用相同幂等键重试");
    }

    private PostingResult postInboundOnce(InboundCommand command) {
        List<InboundLine> lines = sortedDistinctLines(command);
        List<String> keys = lines.stream().map(line -> lineKey(command, line)).toList();
        Set<String> postedKeys = costTransactionRepository.findByIdempotencyKeys(command.corpid(), keys).stream()
            .map(StockCostTransaction::getIdempotencyKey).collect(java.util.stream.Collectors.toSet());
        List<InboundLine> pendingLines = lines.stream().filter(line -> !postedKeys.contains(lineKey(command, line))).toList();
        if (pendingLines.isEmpty()) {
            return new PostingResult(command.idempotencyKey(), BigDecimal.ZERO, 0);
        }

        Map<String, StockBalance> existing = existingBalances(command.corpid(), pendingLines);
        List<StockBalance> balancesToInsert = new ArrayList<>();
        List<StockBalance> balancesToUpdate = new ArrayList<>();
        List<StockTransaction> quantityTransactions = new ArrayList<>();
        List<StockCostTransaction> costTransactions = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        long now = System.currentTimeMillis();

        for (InboundLine line : pendingLines) {
            String balanceKey = balanceKey(line.warehouseId(), line.skuId());
            StockBalance balance = existing.get(balanceKey);
            boolean isNew = balance == null;
            if (isNew) {
                balance = newBalance(command, line, now);
            }
            BigDecimal beforeQty = value(balance.getQty());
            BigDecimal beforeCost = value(balance.getTotalCost());
            BigDecimal beforeUnit = value(balance.getUnitCost());
            CostCalculationResult result = costStrategy.inbound(new CostCalculationContext(line.quantity(), line.totalCost(), beforeQty, beforeCost, beforeUnit));
            applyResult(balance, command.operatorId(), now, result);
            if (isNew) {
                balancesToInsert.add(balance);
            } else {
                balancesToUpdate.add(balance);
            }
            String key = lineKey(command, line);
            quantityTransactions.add(quantityTransaction(command, line, key, beforeQty, result.quantityAfter(), now));
            costTransactions.add(costTransaction(command, line, key, beforeQty, beforeCost, beforeUnit, result, now));
            totalCost = totalCost.add(line.totalCost());
        }
        if (!balancesToInsert.isEmpty()) {
            balanceRepository.insertBatch(balancesToInsert);
        }
        for (StockBalance balance : balancesToUpdate) {
            int expectedVersion = balance.getVersion() - 1;
            if (!balanceRepository.updateWithVersion(balance, expectedVersion)) {
                throw new InventoryConcurrentException();
            }
        }
        transactionRepository.insertBatch(quantityTransactions);
        costTransactionRepository.insertBatch(costTransactions);
        return new PostingResult(command.idempotencyKey(), totalCost, pendingLines.size());
    }

    /**
     * 在单次事务尝试内批量计算、CAS 更新余额并写入出库数量与成本流水。
     */
    private PostingResult postOutboundOnce(OutboundCommand command) {
        List<OutboundLine> lines = sortedDistinctLines(command);
        List<String> keys = lines.stream().map(line -> lineKey(command, line)).toList();
        Set<String> postedKeys = costTransactionRepository.findByIdempotencyKeys(command.corpid(), keys).stream()
            .map(StockCostTransaction::getIdempotencyKey).collect(java.util.stream.Collectors.toSet());
        List<OutboundLine> pendingLines = lines.stream().filter(line -> !postedKeys.contains(lineKey(command, line))).toList();
        if (pendingLines.isEmpty()) {
            return new PostingResult(command.idempotencyKey(), BigDecimal.ZERO, 0);
        }
        Map<String, StockBalance> existing = existingBalancesForOutbound(command.corpid(), pendingLines);
        if (existing.size() != pendingLines.size()) {
            throw new BizException("库存余额不存在");
        }
        List<StockTransaction> quantityTransactions = new ArrayList<>();
        List<StockCostTransaction> costTransactions = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        long now = System.currentTimeMillis();
        for (OutboundLine line : pendingLines) {
            StockBalance balance = existing.get(balanceKey(line.warehouseId(), line.skuId()));
            BigDecimal beforeQty = value(balance.getQty());
            BigDecimal beforeCost = value(balance.getTotalCost());
            BigDecimal beforeUnit = value(balance.getUnitCost());
            if (value(balance.getAvailableQty()).compareTo(line.quantity()) < 0) {
                throw new BizException("可用库存不足");
            }
            CostCalculationResult result = costStrategy.outbound(new CostCalculationContext(line.quantity(), BigDecimal.ZERO, beforeQty, beforeCost, beforeUnit));
            applyResult(balance, command.operatorId(), now, result);
            String key = lineKey(command, line);
            quantityTransactions.add(quantityTransaction(command, line, key, beforeQty, result.quantityAfter(), now));
            costTransactions.add(costTransaction(command, line, key, beforeQty, beforeCost, beforeUnit, result, now));
            totalCost = totalCost.add(result.totalCostChange().abs());
        }
        for (StockBalance balance : existing.values()) {
            int expectedVersion = balance.getVersion() - 1;
            if (!balanceRepository.updateWithVersion(balance, expectedVersion)) {
                throw new InventoryConcurrentException();
            }
        }
        transactionRepository.insertBatch(quantityTransactions);
        costTransactionRepository.insertBatch(costTransactions);
        return new PostingResult(command.idempotencyKey(), totalCost, pendingLines.size());
    }

    private Map<String, StockBalance> existingBalances(String corpid, List<InboundLine> lines) {
        List<StockBalance> keys = lines.stream().map(line -> {
            StockBalance key = new StockBalance(); key.setWarehouseId(line.warehouseId()); key.setSkuId(line.skuId()); return key;
        }).toList();
        Map<String, StockBalance> result = new HashMap<>();
        balanceRepository.findByWarehouseAndSkuPairs(corpid, keys).forEach(balance -> result.put(balanceKey(balance.getWarehouseId(), balance.getSkuId()), balance));
        return result;
    }

    /**
     * 一次批量加载出库涉及的全部余额，避免按行查询。
     */
    private Map<String, StockBalance> existingBalancesForOutbound(String corpid, List<OutboundLine> lines) {
        List<StockBalance> keys = lines.stream().map(line -> {
            StockBalance key = new StockBalance();
            key.setWarehouseId(line.warehouseId());
            key.setSkuId(line.skuId());
            return key;
        }).toList();
        Map<String, StockBalance> result = new HashMap<>();
        balanceRepository.findByWarehouseAndSkuPairs(corpid, keys)
            .forEach(balance -> result.put(balanceKey(balance.getWarehouseId(), balance.getSkuId()), balance));
        return result;
    }

    private static List<InboundLine> sortedDistinctLines(InboundCommand command) {
        Set<String> dimensionKeys = new HashSet<>();
        return command.lines().stream().peek(line -> {
            if (line == null || line.warehouseId() == null || line.skuId() == null || line.sourceLineId() == null
                || line.quantity() == null || line.quantity().signum() <= 0 || line.totalCost() == null || line.totalCost().signum() < 0
                || !dimensionKeys.add(balanceKey(line.warehouseId(), line.skuId()))) {
                throw new BizException("入库明细库存维度重复或数据无效");
            }
        }).sorted(Comparator.comparing(InboundLine::warehouseId).thenComparing(InboundLine::skuId)).toList();
    }

    /**
     * 校验出库明细的库存维度唯一性，并以固定顺序处理以降低死锁概率。
     */
    private static List<OutboundLine> sortedDistinctLines(OutboundCommand command) {
        Set<String> dimensionKeys = new HashSet<>();
        return command.lines().stream().peek(line -> {
            if (line == null || line.warehouseId() == null || line.skuId() == null || line.sourceLineId() == null
                || line.quantity() == null || line.quantity().signum() <= 0
                || !dimensionKeys.add(balanceKey(line.warehouseId(), line.skuId()))) {
                throw new BizException("出库明细库存维度重复或数据无效");
            }
        }).sorted(Comparator.comparing(OutboundLine::warehouseId).thenComparing(OutboundLine::skuId)).toList();
    }

    private static StockBalance newBalance(InboundCommand command, InboundLine line, long now) {
        StockBalance balance = new StockBalance(); balance.setCorpid(command.corpid()); balance.setWarehouseId(line.warehouseId()); balance.setSkuId(line.skuId());
        balance.setLockedQty(BigDecimal.ZERO); balance.setVersion(0); balance.setDel(0); balance.setCreatorId(command.operatorId()); balance.setAddTime(now); return balance;
    }

    private static void applyResult(StockBalance balance, String operatorId, long now, CostCalculationResult result) {
        balance.setQty(result.quantityAfter()); balance.setAvailableQty(result.quantityAfter().subtract(value(balance.getLockedQty())));
        balance.setTotalCost(result.totalCostAfter()); balance.setUnitCost(result.unitCostAfter()); balance.setVersion((balance.getVersion() == null ? 0 : balance.getVersion()) + 1);
        balance.setModifyId(operatorId); balance.setUpdateTime(now);
    }

    private static StockTransaction quantityTransaction(InboundCommand c, InboundLine l, String key, BigDecimal before, BigDecimal after, long now) {
        StockTransaction t = new StockTransaction(); t.setCorpid(c.corpid()); t.setWarehouseId(l.warehouseId()); t.setSkuId(l.skuId()); t.setActionType("INBOUND");
        t.setQtyBefore(before); t.setQtyChange(l.quantity()); t.setQtyAfter(after); t.setSourceType(c.sourceType()); t.setSourceId(c.sourceId()); t.setIdempotencyKey(key);
        t.setOperatorId(c.operatorId()); t.setOccurredAt(c.occurredAt()); t.setDel(0); t.setAddTime(now); t.setUpdateTime(now); t.setCreatorId(c.operatorId()); t.setModifyId(c.operatorId()); return t;
    }

    private static StockCostTransaction costTransaction(InboundCommand c, InboundLine l, String key, BigDecimal beforeQty, BigDecimal beforeCost, BigDecimal beforeUnit, CostCalculationResult r, long now) {
        StockCostTransaction t = new StockCostTransaction(); t.setCorpid(c.corpid()); t.setWarehouseId(l.warehouseId()); t.setSkuId(l.skuId()); t.setActionType("INBOUND");
        t.setBusinessCode(c.businessCode()); t.setSourceId(c.sourceId()); t.setQtyBefore(beforeQty); t.setQtyChange(l.quantity()); t.setQtyAfter(r.quantityAfter());
        t.setTotalCostBefore(beforeCost); t.setTotalCostChange(r.totalCostChange()); t.setTotalCostAfter(r.totalCostAfter()); t.setUnitCostBefore(beforeUnit);
        t.setUnitCost(l.totalCost().divide(l.quantity(), 6, RoundingMode.HALF_UP)); t.setUnitCostAfter(r.unitCostAfter()); t.setTailDifference(BigDecimal.ZERO);
        t.setIdempotencyKey(key); t.setOperatorId(c.operatorId()); t.setOccurredAt(c.occurredAt()); t.setDel(0); t.setAddTime(now); t.setUpdateTime(now); t.setCreatorId(c.operatorId()); t.setModifyId(c.operatorId()); return t;
    }

    /**
     * 构造出库数量流水，数量变化为负数。
     */
    private static StockTransaction quantityTransaction(OutboundCommand c, OutboundLine l, String key, BigDecimal before, BigDecimal after, long now) {
        StockTransaction t = new StockTransaction(); t.setCorpid(c.corpid()); t.setWarehouseId(l.warehouseId()); t.setSkuId(l.skuId()); t.setActionType("OUTBOUND");
        t.setQtyBefore(before); t.setQtyChange(l.quantity().negate()); t.setQtyAfter(after); t.setSourceType(c.sourceType()); t.setSourceId(c.sourceId()); t.setIdempotencyKey(key);
        t.setOperatorId(c.operatorId()); t.setOccurredAt(c.occurredAt()); t.setDel(0); t.setAddTime(now); t.setUpdateTime(now); t.setCreatorId(c.operatorId()); t.setModifyId(c.operatorId()); return t;
    }

    /**
     * 构造出库成本流水，成本变化为按策略结转的负数。
     */
    private static StockCostTransaction costTransaction(OutboundCommand c, OutboundLine l, String key, BigDecimal beforeQty, BigDecimal beforeCost, BigDecimal beforeUnit, CostCalculationResult r, long now) {
        StockCostTransaction t = new StockCostTransaction(); t.setCorpid(c.corpid()); t.setWarehouseId(l.warehouseId()); t.setSkuId(l.skuId()); t.setActionType("OUTBOUND");
        t.setBusinessCode(c.businessCode()); t.setSourceId(c.sourceId()); t.setQtyBefore(beforeQty); t.setQtyChange(l.quantity().negate()); t.setQtyAfter(r.quantityAfter());
        t.setTotalCostBefore(beforeCost); t.setTotalCostChange(r.totalCostChange()); t.setTotalCostAfter(r.totalCostAfter()); t.setUnitCostBefore(beforeUnit);
        t.setUnitCost(beforeUnit); t.setUnitCostAfter(r.unitCostAfter()); t.setTailDifference(BigDecimal.ZERO); t.setIdempotencyKey(key); t.setOperatorId(c.operatorId());
        t.setOccurredAt(c.occurredAt()); t.setDel(0); t.setAddTime(now); t.setUpdateTime(now); t.setCreatorId(c.operatorId()); t.setModifyId(c.operatorId()); return t;
    }

    private static String lineKey(InboundCommand command, InboundLine line) { return command.idempotencyKey() + ":" + line.sourceLineId(); }
    private static String lineKey(OutboundCommand command, OutboundLine line) { return command.idempotencyKey() + ":" + line.sourceLineId(); }
    private static String balanceKey(Long warehouseId, Long skuId) { return warehouseId + ":" + skuId; }
    private static BigDecimal value(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private static DefaultTransactionDefinition requiredDefinition() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return definition;
    }

    private static void validate(InboundCommand command) {
        if (command == null || command.corpid() == null || command.corpid().isBlank() || command.sourceId() == null
            || command.idempotencyKey() == null || command.idempotencyKey().isBlank() || command.lines() == null || command.lines().isEmpty()) {
            throw new BizException("库存入库参数不完整");
        }
    }

    /**
     * 校验出库命令的公共上下文；行级数量和库存维度在后续批量预处理时校验。
     */
    private static void validate(OutboundCommand command) {
        if (command == null || command.corpid() == null || command.corpid().isBlank() || command.sourceId() == null
            || command.idempotencyKey() == null || command.idempotencyKey().isBlank() || command.lines() == null || command.lines().isEmpty()) {
            throw new BizException("库存出库参数不完整");
        }
    }
}
