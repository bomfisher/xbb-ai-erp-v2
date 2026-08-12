package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseInboundConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInboundMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInboundPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModulePurchasePurchaseInboundRepositoryImpl")
@RequiredArgsConstructor
public class PurchaseInboundRepositoryImpl implements PurchaseInboundRepository {

    private final PurchaseInboundMapper purchaseInboundMapper;

    @Override
    public Long insert(PurchaseInbound purchaseInbound) {
        PurchaseInboundPO po = PurchaseInboundConvertor.toPO(purchaseInbound);
        po.setId(null);
        purchaseInboundMapper.insert(po);
        purchaseInbound.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<PurchaseInbound> purchaseInboundList) {
        List<PurchaseInboundPO> poList = purchaseInboundList.stream().map(PurchaseInboundConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        purchaseInboundMapper.insertBatch(poList);
        for (int index = 0; index < purchaseInboundList.size(); index++) {
            purchaseInboundList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseInboundMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseInboundMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseInbound purchaseInbound) {
        PurchaseInboundPO po = PurchaseInboundConvertor.toPO(purchaseInbound);
        purchaseInboundMapper.update(po);
    }

    @Override
    public PurchaseInbound findById(String corpid, Long id) {
        return PurchaseInboundConvertor.toDomain(purchaseInboundMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseInbound> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInboundMapper.findByCondition(preparedConditionMap).stream().map(PurchaseInboundConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInboundMapper.count(preparedConditionMap);
    }
}
