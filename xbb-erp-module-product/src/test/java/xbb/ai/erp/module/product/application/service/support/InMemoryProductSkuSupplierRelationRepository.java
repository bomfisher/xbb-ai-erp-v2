package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationListItem;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;
import xbb.ai.erp.module.product.domain.repository.ProductSkuSupplierRelationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductSkuSupplierRelationRepository implements ProductSkuSupplierRelationRepository {

    private final List<ProductSkuSupplierRelation> relationData = new ArrayList<>();
    private final List<ProductSkuSupplierRelationHistory> historyData = new ArrayList<>();
    private final List<ProductSkuSupplierRelationSkuOption> skuOptions = new ArrayList<>();
    private final List<ProductSkuSupplierRelationSupplierOption> supplierOptions = new ArrayList<>();
    private final AtomicLong relationSequence = new AtomicLong(1);
    private final AtomicLong historySequence = new AtomicLong(1);

    public void seedRelation(ProductSkuSupplierRelation relation) {
        relationData.add(relation);
        relationSequence.updateAndGet(current -> Math.max(current, relation.getId() == null ? current : relation.getId() + 1));
    }

    public void seedSkuOption(ProductSkuSupplierRelationSkuOption option) {
        skuOptions.add(option);
    }

    public void seedSupplierOption(ProductSkuSupplierRelationSupplierOption option) {
        supplierOptions.add(option);
    }

    public List<ProductSkuSupplierRelation> allRelations() {
        return relationData;
    }

    public List<ProductSkuSupplierRelationHistory> allHistory() {
        return historyData;
    }

    @Override
    public Long save(ProductSkuSupplierRelation relation, String userId) {
        if (relation.getId() == null) {
            relation.setId(relationSequence.getAndIncrement());
        }
        relation.setCreatorId(userId);
        relation.setModifyId(userId);
        relation.setDel(0);
        relation.setVersion(relation.getVersion() == null ? 0 : relation.getVersion());
        long now = System.currentTimeMillis();
        relation.setAddTime(now);
        relation.setUpdateTime(now);
        relationData.add(relation);
        return relation.getId();
    }

    @Override
    public void update(ProductSkuSupplierRelation relation, String userId) {
        removeById(relation.getCorpid(), relation.getId(), userId);
        relation.setModifyId(userId);
        relation.setDel(0);
        relation.setUpdateTime(System.currentTimeMillis());
        relationData.add(relation);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        relationData.removeIf(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(id, item.getId()));
    }

    @Override
    public void updateDefaultFlag(String corpid, Long id, Integer defaultFlag, String userId) {
        relationData.stream()
            .filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(id, item.getId()))
            .findFirst()
            .ifPresent(item -> {
                item.setDefaultFlag(defaultFlag);
                item.setModifyId(userId);
                item.setUpdateTime(System.currentTimeMillis());
            });
    }

    @Override
    public void clearDefaultBySkuId(String corpid, Long skuId, Long excludeId, String userId) {
        relationData.stream()
            .filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(skuId, item.getSkuId()) && !Objects.equals(excludeId, item.getId()))
            .forEach(item -> {
                item.setDefaultFlag(0);
                item.setModifyId(userId);
                item.setUpdateTime(System.currentTimeMillis());
            });
    }

    @Override
    public void updateEnableStatus(String corpid, Long id, Integer enableStatus, String userId) {
        relationData.stream()
            .filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(id, item.getId()))
            .findFirst()
            .ifPresent(item -> {
                item.setEnableStatus(enableStatus);
                item.setModifyId(userId);
                item.setUpdateTime(System.currentTimeMillis());
            });
    }

    @Override
    public ProductSkuSupplierRelation findById(String corpid, Long id) {
        return relationData.stream()
            .filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(id, item.getId()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<ProductSkuSupplierRelationListItem> findListByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object spuId = conditionMap.get("spuId");
        Object supplierId = conditionMap.get("supplierId");
        Object offset = conditionMap.get("offset");
        Object pageSize = conditionMap.get("pageSize");
        List<ProductSkuSupplierRelationListItem> list = relationData.stream()
            .filter(item -> corpid == null || Objects.equals(corpid, item.getCorpid()))
            .filter(item -> supplierId == null || Objects.equals(supplierId, item.getSupplierId()))
            .filter(item -> spuId == null || skuOptions.stream().anyMatch(option -> Objects.equals(option.getSkuId(), item.getSkuId()) && Objects.equals(option.getSpuId(), spuId)))
            .map(this::toListItem)
            .sorted(Comparator.comparing(ProductSkuSupplierRelationListItem::getId).reversed())
            .toList();
        if (pageSize instanceof Integer size && size > 0) {
            int start = offset instanceof Integer integerOffset ? Math.max(integerOffset, 0) : 0;
            if (start >= list.size()) {
                return List.of();
            }
            int end = Math.min(start + size, list.size());
            return list.subList(start, end);
        }
        return list;
    }

    @Override
    public long countListByCondition(Map<String, Object> conditionMap) {
        java.util.HashMap<String, Object> countCondition = new java.util.HashMap<>();
        countCondition.put("corpid", conditionMap.get("corpid"));
        countCondition.put("spuId", conditionMap.get("spuId"));
        countCondition.put("supplierId", conditionMap.get("supplierId"));
        return findListByCondition(countCondition).size();
    }

    @Override
    public List<ProductSkuSupplierRelationSkuOption> findSkuOptionsBySpu(String corpid, Long spuId) {
        return skuOptions.stream()
            .filter(item -> Objects.equals(spuId, item.getSpuId()))
            .toList();
    }

    @Override
    public ProductSkuSupplierRelationSkuOption findSkuOptionById(String corpid, Long skuId) {
        return skuOptions.stream().filter(item -> Objects.equals(skuId, item.getSkuId())).findFirst().orElse(null);
    }

    @Override
    public List<ProductSkuSupplierRelationSupplierOption> findSupplierOptions(String corpid, Long supplierId, Integer limit) {
        List<ProductSkuSupplierRelationSupplierOption> list = supplierOptions.stream()
            .filter(item -> supplierId == null || Objects.equals(supplierId, item.getSupplierId()))
            .toList();
        if (limit == null || limit <= 0 || list.size() <= limit) {
            return list;
        }
        return list.subList(0, limit);
    }

    @Override
    public void insertHistory(ProductSkuSupplierRelationHistory history) {
        if (history.getId() == null) {
            history.setId(historySequence.getAndIncrement());
        }
        history.setDel(0);
        history.setCreatorId(history.getOperatorId());
        history.setModifyId(history.getOperatorId());
        long now = System.currentTimeMillis();
        history.setAddTime(now);
        history.setUpdateTime(now);
        historyData.add(history);
    }

    @Override
    public List<ProductSkuSupplierRelationHistory> findHistoryByRelationId(String corpid, Long relationId) {
        return historyData.stream()
            .filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(relationId, item.getRelationId()))
            .sorted(Comparator.comparing(ProductSkuSupplierRelationHistory::getId).reversed())
            .toList();
    }

    private ProductSkuSupplierRelationListItem toListItem(ProductSkuSupplierRelation relation) {
        ProductSkuSupplierRelationListItem item = new ProductSkuSupplierRelationListItem();
        item.setId(relation.getId());
        item.setSkuId(relation.getSkuId());
        ProductSkuSupplierRelationSkuOption skuOption = findSkuOptionById(relation.getCorpid(), relation.getSkuId());
        if (skuOption != null) {
            item.setSkuCode(skuOption.getSkuCode());
            item.setSkuName(skuOption.getSkuName());
            item.setSpecSnapshot(skuOption.getSpecSnapshot());
        }
        item.setSupplierId(relation.getSupplierId());
        ProductSkuSupplierRelationSupplierOption supplierOption = supplierOptions.stream().filter(option -> Objects.equals(option.getSupplierId(), relation.getSupplierId())).findFirst().orElse(null);
        if (supplierOption != null) {
            item.setSupplierCode(supplierOption.getSupplierCode());
            item.setSupplierName(supplierOption.getSupplierName());
        }
        item.setPurchasePrice(relation.getPurchasePrice());
        item.setDeliveryCycleDay(relation.getDeliveryCycleDay());
        item.setMinOrderQty(relation.getMinOrderQty());
        item.setSupplierSkuCode(relation.getSupplierSkuCode());
        item.setDefaultFlag(relation.getDefaultFlag());
        item.setEnableStatus(relation.getEnableStatus());
        item.setRemark(relation.getRemark());
        item.setUpdateTime(relation.getUpdateTime());
        return item;
    }
}
