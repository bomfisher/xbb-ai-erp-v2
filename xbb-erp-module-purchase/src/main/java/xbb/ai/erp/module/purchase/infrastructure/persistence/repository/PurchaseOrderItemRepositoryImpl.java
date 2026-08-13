package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseOrderItemConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseOrderItemMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModulePurchasePurchaseOrderItemRepositoryImpl")
@RequiredArgsConstructor
public class PurchaseOrderItemRepositoryImpl implements PurchaseOrderItemRepository {

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Override
    public Long insert(PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItemPO po = PurchaseOrderItemConvertor.toPO(purchaseOrderItem);
        initializeForInsert(po);
        purchaseOrderItemMapper.insert(po);
        purchaseOrderItem.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<PurchaseOrderItem> purchaseOrderItemList) {
        List<PurchaseOrderItemPO> poList = purchaseOrderItemList.stream().map(PurchaseOrderItemConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        purchaseOrderItemMapper.insertBatch(poList);
        for (int index = 0; index < purchaseOrderItemList.size(); index++) {
            purchaseOrderItemList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseOrderItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseOrderItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItemPO po = PurchaseOrderItemConvertor.toPO(purchaseOrderItem);
        po.setUpdateTime(System.currentTimeMillis());
        purchaseOrderItemMapper.update(po);
    }

    @Override
    public PurchaseOrderItem findById(String corpid, Long id) {
        return PurchaseOrderItemConvertor.toDomain(purchaseOrderItemMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseOrderItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseOrderItemMapper.findByCondition(preparedConditionMap).stream().map(PurchaseOrderItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseOrderItemMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
