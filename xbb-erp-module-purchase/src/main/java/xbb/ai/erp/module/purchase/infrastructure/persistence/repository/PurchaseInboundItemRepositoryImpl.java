package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseInboundItemConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInboundItemMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInboundItemPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchaseInboundItemRepositoryImpl implements PurchaseInboundItemRepository {

    private final PurchaseInboundItemMapper purchaseInboundItemMapper;

    @Override
    public void insert(PurchaseInboundItem purchaseInboundItem) {
        purchaseInboundItemMapper.insert(PurchaseInboundItemConvertor.toPO(purchaseInboundItem));
    }

    @Override
    public void insertBatch(List<PurchaseInboundItem> purchaseInboundItemList) {
        purchaseInboundItemMapper.insertBatch(purchaseInboundItemList.stream().map(PurchaseInboundItemConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseInboundItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseInboundItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseInboundItem purchaseInboundItem) {
        PurchaseInboundItemPO po = PurchaseInboundItemConvertor.toPO(purchaseInboundItem);
        purchaseInboundItemMapper.update(po);
    }

    @Override
    public PurchaseInboundItem findById(String corpid, Long id) {
        return PurchaseInboundItemConvertor.toDomain(purchaseInboundItemMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseInboundItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInboundItemMapper.findByCondition(preparedConditionMap).stream().map(PurchaseInboundItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInboundItemMapper.count(preparedConditionMap);
    }
}
