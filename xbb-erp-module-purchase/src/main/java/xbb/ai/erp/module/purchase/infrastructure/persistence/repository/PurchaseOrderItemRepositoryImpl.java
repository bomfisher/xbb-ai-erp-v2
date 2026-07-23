package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseOrderItemConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseOrderItemMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchaseOrderItemRepositoryImpl implements PurchaseOrderItemRepository {

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Override
    public void insert(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItemMapper.insert(PurchaseOrderItemConvertor.toPO(purchaseOrderItem));
    }

    @Override
    public void insertBatch(List<PurchaseOrderItem> purchaseOrderItemList) {
        purchaseOrderItemMapper.insertBatch(purchaseOrderItemList.stream().map(PurchaseOrderItemConvertor::toPO).toList());
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
}
