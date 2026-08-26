package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseOrderConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseOrderMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModulePurchasePurchaseOrderRepositoryImpl")
@RequiredArgsConstructor
public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepository {

    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public Long insert(PurchaseOrder purchaseOrder) {
        PurchaseOrderPO po = PurchaseOrderConvertor.toPO(purchaseOrder);
        po.setId(null);
        purchaseOrderMapper.insert(po);
        purchaseOrder.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<PurchaseOrder> purchaseOrderList) {
        List<PurchaseOrderPO> poList = purchaseOrderList.stream().map(PurchaseOrderConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        purchaseOrderMapper.insertBatch(poList);
        for (int index = 0; index < purchaseOrderList.size(); index++) {
            purchaseOrderList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseOrderMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseOrderMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseOrder purchaseOrder) {
        PurchaseOrderPO po = PurchaseOrderConvertor.toPO(purchaseOrder);
        purchaseOrderMapper.update(po);
    }

    @Override
    public PurchaseOrder findById(String corpid, Long id) {
        return PurchaseOrderConvertor.toDomain(purchaseOrderMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseOrder> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return purchaseOrderMapper.findByIds(corpid, ids).stream().map(PurchaseOrderConvertor::toDomain).toList();
    }

    @Override
    public List<PurchaseOrder> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseOrderMapper.findByCondition(preparedConditionMap).stream().map(PurchaseOrderConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseOrderMapper.count(preparedConditionMap);
    }
}
