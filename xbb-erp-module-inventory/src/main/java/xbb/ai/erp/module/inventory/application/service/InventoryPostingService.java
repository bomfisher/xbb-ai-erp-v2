package xbb.ai.erp.module.inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.inventory.contract.InboundCommand;
import xbb.ai.erp.module.inventory.contract.InboundLine;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.OutboundCommand;
import xbb.ai.erp.module.inventory.contract.OutboundLine;
import xbb.ai.erp.module.inventory.contract.PostingResult;
import xbb.ai.erp.module.inventory.contract.ReleaseReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationLine;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationContext;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationResult;
import xbb.ai.erp.module.inventory.domain.cost.CostCalculationStrategy;
import xbb.ai.erp.module.inventory.domain.cost.MovingWeightedAverageCostStrategy;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.model.StockCostTransaction;
import xbb.ai.erp.module.inventory.domain.model.StockTransaction;
import xbb.ai.erp.module.inventory.domain.model.StockReservation;
import xbb.ai.erp.module.inventory.domain.enums.StockReservationStatusEnum;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockCostTransactionRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockReservationRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InventoryPostingService implements InventoryCommandApi {
    private final StockBalanceRepository balanceRepository;
    private final StockTransactionRepository transactionRepository;
    private final StockCostTransactionRepository costTransactionRepository;
    private final StockReservationRepository reservationRepository;
    private final PlatformTransactionManager transactionManager;
    private final CostCalculationStrategy costStrategy = new MovingWeightedAverageCostStrategy();

    @Override
    @Transactional(readOnly = true)
    public void validateOutbound(OutboundCommand command) {
        validate(command);
        validateOutboundAvailability(command, sortedDistinctLines(command));
    }

    @Override
    @Transactional
    public PostingResult reserve(ReservationCommand command) {
        validate(command);
        List<ReservationLine> lines = sortedDistinctLines(command);
        Map<String, StockBalance> balances = lockedReservationLineBalances(command.corpid(), lines);
        List<StockReservation> existingReservations = reservationRepository.findBySourceForUpdate(
            command.corpid(), command.sourceType(), command.sourceId());
        if (!existingReservations.isEmpty()) {
            if (isSameReservation(command, lines, existingReservations)) {
                return new PostingResult(command.idempotencyKey(), BigDecimal.ZERO, 0);
            }
            throw new BizException("该来源单据已存在锁库记录");
        }
        validateReservationAvailability(balances, lines);
        long now = System.currentTimeMillis();
        List<StockReservation> reservations = new ArrayList<>();
        for (ReservationLine line : lines) {
            StockBalance balance = balances.get(balanceKey(line.warehouseId(), line.skuId()));
            balance.setLockedQty(value(balance.getLockedQty()).add(line.quantity()));
            balance.setAvailableQty(value(balance.getQty()).subtract(value(balance.getLockedQty())));
            touch(balance, command.operatorId(), now);
            reservations.add(newReservation(command, line, now));
        }
        balances.values().forEach(balanceRepository::update);
        reservationRepository.insertBatch(reservations);
        return new PostingResult(command.idempotencyKey(), BigDecimal.ZERO, lines.size());
    }

    @Override
    @Transactional
    public PostingResult postReservedOutbound(OutboundCommand command) {
        validate(command);
        List<OutboundLine> lines = sortedDistinctLines(command);
        List<String> keys = lines.stream().map(line -> lineKey(command, line)).toList();
        Set<String> postedKeys = costTransactionRepository.findByIdempotencyKeys(command.corpid(), keys).stream()
            .map(StockCostTransaction::getIdempotencyKey).collect(java.util.stream.Collectors.toSet());
        List<OutboundLine> pendingLines = lines.stream().filter(line -> !postedKeys.contains(lineKey(command, line))).toList();
        if (pendingLines.isEmpty()) {
            return new PostingResult(command.idempotencyKey(), BigDecimal.ZERO, 0);
        }
        Map<String, StockBalance> balances = lockedOutboundLineBalances(command.corpid(), pendingLines);
        List<StockReservation> reservations = reservationRepository.findBySourceForUpdate(
            command.corpid(), command.sourceType(), command.sourceId());
        Map<Long, StockReservation> reservationsByLineId = new HashMap<>();
        reservations.forEach(reservation -> reservationsByLineId.put(reservation.getSourceLineId(), reservation));
        validateOutboundAvailability(balances, reservationsByLineId, pendingLines);
        List<StockTransaction> quantityTransactions = new ArrayList<>();
        List<StockCostTransaction> costTransactions = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        long now = System.currentTimeMillis();
        for (OutboundLine line : pendingLines) {
            StockBalance balance = balances.get(balanceKey(line.warehouseId(), line.skuId()));
            StockReservation reservation = reservationsByLineId.get(line.sourceLineId());
            BigDecimal beforeQty = value(balance.getQty());
            BigDecimal beforeCost = value(balance.getTotalCost());
            BigDecimal beforeUnit = value(balance.getUnitCost());
            CostCalculationResult result = costStrategy.outbound(new CostCalculationContext(line.quantity(), BigDecimal.ZERO,
                beforeQty, beforeCost, beforeUnit));
            if (reservation != null) {
                balance.setLockedQty(value(balance.getLockedQty()).subtract(line.quantity()));
            }
            applyResult(balance, command.operatorId(), now, result);
            if (reservation != null) {
                consumeReservation(reservation, line.quantity(), command.operatorId(), now);
            }
            String key = lineKey(command, line);
            quantityTransactions.add(quantityTransaction(command, line, key, beforeQty, result.quantityAfter(), now));
            costTransactions.add(costTransaction(command, line, key, beforeQty, beforeCost, beforeUnit, result, now));
            totalCost = totalCost.add(result.totalCostChange().abs());
        }
        balances.values().forEach(balanceRepository::update);
        reservations.forEach(reservationRepository::update);
        transactionRepository.insertBatch(quantityTransactions);
        costTransactionRepository.insertBatch(costTransactions);
        return new PostingResult(command.idempotencyKey(), totalCost, pendingLines.size());
    }

    private void validateOutboundAvailability(OutboundCommand command, List<OutboundLine> lines) {
        Map<String, StockBalance> balances = lockedOutboundLineBalances(command.corpid(), lines);
        List<StockReservation> reservations = reservationRepository.findBySourceForUpdate(
            command.corpid(), command.sourceType(), command.sourceId());
        Map<Long, StockReservation> reservationsByLineId = new HashMap<>();
        reservations.forEach(reservation -> reservationsByLineId.put(reservation.getSourceLineId(), reservation));
        validateOutboundAvailability(balances, reservationsByLineId, lines);
    }

    private void validateOutboundAvailability(Map<String, StockBalance> balances,
                                              Map<Long, StockReservation> reservationsByLineId,
                                              List<OutboundLine> lines) {
        List<String> validationErrors = new ArrayList<>();
        for (OutboundLine line : lines) {
            StockReservation reservation = reservationsByLineId.get(line.sourceLineId());
            StockBalance balance = balances.get(balanceKey(line.warehouseId(), line.skuId()));
            if (balance == null) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId() + "：库存余额不存在");
                continue;
            }
            if (reservation == null) {
                if (value(balance.getAvailableQty()).compareTo(line.quantity()) < 0) {
                    validationErrors.add(formatAvailableQuantityShortage(line, balance));
                }
                continue;
            }
            if (reservation.getWarehouseId().longValue() != line.warehouseId()
                || reservation.getSkuId().longValue() != line.skuId()
                || !StockReservationStatusEnum.require(reservation.getStatus()).canConsume()
                || value(reservation.getRemainingQty()).compareTo(line.quantity()) < 0) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId() + "：锁库记录不可出库");
                continue;
            }
            if (value(balance.getLockedQty()).compareTo(line.quantity()) < 0) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId()
                    + "：锁定库存不足（锁定=" + value(balance.getLockedQty()) + "，需出库=" + line.quantity() + "）");
            }
        }
        throwIfInventoryValidationFailed(validationErrors);
    }

    private String formatAvailableQuantityShortage(OutboundLine line, StockBalance balance) {
        BigDecimal availableQty = value(balance.getAvailableQty());
        return "仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId()
            + "：可用库存不足（可用=" + availableQty + "，需出库=" + line.quantity()
            + "，缺少=" + line.quantity().subtract(availableQty) + "）";
    }

    private void validateReservationAvailability(Map<String, StockBalance> balances, List<ReservationLine> lines) {
        List<String> validationErrors = new ArrayList<>();
        for (ReservationLine line : lines) {
            StockBalance balance = balances.get(balanceKey(line.warehouseId(), line.skuId()));
            if (balance == null) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId() + "：库存余额不存在");
                continue;
            }
            BigDecimal availableQty = value(balance.getAvailableQty());
            if (availableQty.compareTo(line.quantity()) < 0) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId()
                    + "：可用库存不足（可用=" + availableQty + "，需锁库=" + line.quantity()
                    + "，缺少=" + line.quantity().subtract(availableQty) + "）");
            }
        }
        throwIfInventoryValidationFailed(validationErrors);
    }

    private void validateDirectOutboundAvailability(Map<String, StockBalance> balances, List<OutboundLine> lines) {
        List<String> validationErrors = new ArrayList<>();
        for (OutboundLine line : lines) {
            StockBalance balance = balances.get(balanceKey(line.warehouseId(), line.skuId()));
            if (balance == null) {
                validationErrors.add("仓库ID=" + line.warehouseId() + "，产品ID=" + line.skuId() + "：库存余额不存在");
                continue;
            }
            if (value(balance.getAvailableQty()).compareTo(line.quantity()) < 0) {
                validationErrors.add(formatAvailableQuantityShortage(line, balance));
            }
        }
        throwIfInventoryValidationFailed(validationErrors);
    }

    private void throwIfInventoryValidationFailed(List<String> validationErrors) {
        if (!validationErrors.isEmpty()) {
            throw new BizException("库存校验失败：" + String.join("；", validationErrors));
        }
    }

    @Override
    @Transactional
    public void releaseReservation(ReleaseReservationCommand command) {
        validate(command);
        List<StockReservation> candidates = reservationRepository.findBySource(
            command.corpid(), command.sourceType(), command.sourceId());
        if (candidates.isEmpty()) {
            return;
        }
        Map<String, StockBalance> balances = lockedReservationBalances(command.corpid(), candidates);
        List<StockReservation> reservations = reservationRepository.findBySourceForUpdate(
            command.corpid(), command.sourceType(), command.sourceId());
        long now = System.currentTimeMillis();
        for (StockReservation reservation : reservations) {
            if (!StockReservationStatusEnum.require(reservation.getStatus()).canRelease()) {
                continue;
            }
            StockBalance balance = balances.get(balanceKey(reservation.getWarehouseId(), reservation.getSkuId()));
            BigDecimal remainingQty = value(reservation.getRemainingQty());
            if (value(balance.getLockedQty()).compareTo(remainingQty) < 0) {
                throw new BizException("锁定库存不足");
            }
            balance.setLockedQty(value(balance.getLockedQty()).subtract(remainingQty));
            balance.setAvailableQty(value(balance.getQty()).subtract(value(balance.getLockedQty())));
            touch(balance, command.operatorId(), now);
            reservation.setReleasedQty(value(reservation.getReleasedQty()).add(remainingQty));
            reservation.setRemainingQty(BigDecimal.ZERO);
            reservation.setStatus(StockReservationStatusEnum.RELEASED.name());
            reservation.setReleasedAt(command.occurredAt());
            reservation.setVersion(nextVersion(reservation.getVersion()));
            reservation.setModifyId(command.operatorId());
            reservation.setUpdateTime(now);
        }
        balances.values().forEach(balanceRepository::update);
        reservations.forEach(reservationRepository::update);
    }

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
        validateDirectOutboundAvailability(existing, pendingLines);
        List<StockTransaction> quantityTransactions = new ArrayList<>();
        List<StockCostTransaction> costTransactions = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        long now = System.currentTimeMillis();
        for (OutboundLine line : pendingLines) {
            StockBalance balance = existing.get(balanceKey(line.warehouseId(), line.skuId()));
            BigDecimal beforeQty = value(balance.getQty());
            BigDecimal beforeCost = value(balance.getTotalCost());
            BigDecimal beforeUnit = value(balance.getUnitCost());
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

    private Map<String, StockBalance> lockedReservationLineBalances(String corpid, List<ReservationLine> lines) {
        List<StockBalance> keys = lines.stream().map(line -> stockKey(line.warehouseId(), line.skuId())).toList();
        return toBalanceMap(balanceRepository.findByWarehouseAndSkuPairsForUpdate(corpid, keys));
    }

    private Map<String, StockBalance> lockedOutboundLineBalances(String corpid, List<OutboundLine> lines) {
        List<StockBalance> keys = lines.stream().map(line -> stockKey(line.warehouseId(), line.skuId())).toList();
        return toBalanceMap(balanceRepository.findByWarehouseAndSkuPairsForUpdate(corpid, keys));
    }

    private Map<String, StockBalance> lockedReservationBalances(String corpid, List<StockReservation> reservations) {
        List<StockBalance> keys = reservations.stream().map(reservation -> stockKey(
            reservation.getWarehouseId(), reservation.getSkuId())).sorted(Comparator.comparing(StockBalance::getWarehouseId)
            .thenComparing(StockBalance::getSkuId)).toList();
        return toBalanceMap(balanceRepository.findByWarehouseAndSkuPairsForUpdate(corpid, keys));
    }

    private static StockBalance stockKey(Long warehouseId, Long skuId) {
        StockBalance key = new StockBalance();
        key.setWarehouseId(warehouseId);
        key.setSkuId(skuId);
        return key;
    }

    private static Map<String, StockBalance> toBalanceMap(List<StockBalance> balances) {
        Map<String, StockBalance> result = new LinkedHashMap<>();
        balances.forEach(balance -> result.put(balanceKey(balance.getWarehouseId(), balance.getSkuId()), balance));
        return result;
    }

    private static StockReservation newReservation(ReservationCommand command, ReservationLine line, long now) {
        StockReservation reservation = new StockReservation();
        reservation.setCorpid(command.corpid());
        reservation.setWarehouseId(line.warehouseId());
        reservation.setSkuId(line.skuId());
        reservation.setSourceType(command.sourceType());
        reservation.setSourceId(command.sourceId());
        reservation.setSourceLineId(line.sourceLineId());
        reservation.setReservedQty(line.quantity());
        reservation.setOutboundQty(BigDecimal.ZERO);
        reservation.setReleasedQty(BigDecimal.ZERO);
        reservation.setRemainingQty(line.quantity());
        reservation.setStatus(StockReservationStatusEnum.RESERVED.name());
        reservation.setReservedAt(command.occurredAt());
        reservation.setIdempotencyKey(reservationLineKey(command, line));
        reservation.setVersion(0);
        reservation.setDel(0);
        reservation.setAddTime(now);
        reservation.setUpdateTime(now);
        reservation.setCreatorId(command.operatorId());
        reservation.setModifyId(command.operatorId());
        return reservation;
    }

    private static void consumeReservation(StockReservation reservation, BigDecimal quantity, String operatorId, long now) {
        BigDecimal remainingQty = value(reservation.getRemainingQty()).subtract(quantity);
        reservation.setOutboundQty(value(reservation.getOutboundQty()).add(quantity));
        reservation.setRemainingQty(remainingQty);
        reservation.setStatus(remainingQty.signum() == 0 ? StockReservationStatusEnum.FULLY_OUTBOUNDED.name()
            : StockReservationStatusEnum.PARTIALLY_OUTBOUNDED.name());
        reservation.setVersion(nextVersion(reservation.getVersion()));
        reservation.setModifyId(operatorId);
        reservation.setUpdateTime(now);
    }

    private static boolean isSameReservation(ReservationCommand command, List<ReservationLine> lines,
                                             List<StockReservation> reservations) {
        if (lines.size() != reservations.size()) {
            return false;
        }
        Map<Long, StockReservation> reservationsByLineId = new HashMap<>();
        reservations.forEach(reservation -> reservationsByLineId.put(reservation.getSourceLineId(), reservation));
        return lines.stream().allMatch(line -> {
            StockReservation reservation = reservationsByLineId.get(line.sourceLineId());
            return reservation != null && reservation.getWarehouseId().equals(line.warehouseId())
                && reservation.getSkuId().equals(line.skuId()) && value(reservation.getReservedQty()).compareTo(line.quantity()) == 0
                && reservationLineKey(command, line).equals(reservation.getIdempotencyKey());
        });
    }

    private static void touch(StockBalance balance, String operatorId, long now) {
        balance.setVersion(nextVersion(balance.getVersion()));
        balance.setModifyId(operatorId);
        balance.setUpdateTime(now);
    }

    private static int nextVersion(Integer version) {
        return version == null ? 1 : version + 1;
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

    private static List<ReservationLine> sortedDistinctLines(ReservationCommand command) {
        Set<String> dimensionKeys = new HashSet<>();
        return command.lines().stream().peek(line -> {
            if (line == null || line.warehouseId() == null || line.skuId() == null || line.sourceLineId() == null
                || line.quantity() == null || line.quantity().signum() <= 0
                || !dimensionKeys.add(balanceKey(line.warehouseId(), line.skuId()))) {
                throw new BizException("锁库明细库存维度重复或数据无效");
            }
        }).sorted(Comparator.comparing(ReservationLine::warehouseId).thenComparing(ReservationLine::skuId)).toList();
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
    private static String reservationLineKey(ReservationCommand command, ReservationLine line) {
        return command.idempotencyKey() + ":" + line.sourceLineId();
    }
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

    private static void validate(ReservationCommand command) {
        if (command == null || command.corpid() == null || command.corpid().isBlank() || command.sourceId() == null
            || command.sourceType() == null || command.sourceType().isBlank() || command.idempotencyKey() == null
            || command.idempotencyKey().isBlank() || command.lines() == null || command.lines().isEmpty()) {
            throw new BizException("库存锁库参数不完整");
        }
    }

    private static void validate(ReleaseReservationCommand command) {
        if (command == null || command.corpid() == null || command.corpid().isBlank() || command.sourceId() == null
            || command.sourceType() == null || command.sourceType().isBlank() || command.idempotencyKey() == null
            || command.idempotencyKey().isBlank()) {
            throw new BizException("释放锁库参数不完整");
        }
    }
}
