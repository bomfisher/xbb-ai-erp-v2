package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseRequestItemConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseRequestItemMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestItemPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchaseRequestItemRepositoryImpl implements PurchaseRequestItemRepository {

    private final PurchaseRequestItemMapper purchaseRequestItemMapper;

    @Override
    public void insert(PurchaseRequestItem purchaseRequestItem) {
        purchaseRequestItemMapper.insert(PurchaseRequestItemConvertor.toPO(purchaseRequestItem));
    }

    @Override
    public void insertBatch(List<PurchaseRequestItem> purchaseRequestItemList) {
        purchaseRequestItemMapper.insertBatch(purchaseRequestItemList.stream().map(PurchaseRequestItemConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseRequestItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseRequestItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseRequestItem purchaseRequestItem) {
        PurchaseRequestItemPO po = PurchaseRequestItemConvertor.toPO(purchaseRequestItem);
        purchaseRequestItemMapper.update(po);
    }

    @Override
    public PurchaseRequestItem findById(String corpid, Long id) {
        return PurchaseRequestItemConvertor.toDomain(purchaseRequestItemMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseRequestItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseRequestItemMapper.findByCondition(preparedConditionMap).stream().map(PurchaseRequestItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseRequestItemMapper.count(preparedConditionMap);
    }
}
