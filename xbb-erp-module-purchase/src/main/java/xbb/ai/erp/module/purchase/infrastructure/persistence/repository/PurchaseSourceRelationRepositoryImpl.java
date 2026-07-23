package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseSourceRelationRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseSourceRelationConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseSourceRelationMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseSourceRelationPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchaseSourceRelationRepositoryImpl implements PurchaseSourceRelationRepository {

    private final PurchaseSourceRelationMapper purchaseSourceRelationMapper;

    @Override
    public void insert(PurchaseSourceRelation purchaseSourceRelation) {
        purchaseSourceRelationMapper.insert(PurchaseSourceRelationConvertor.toPO(purchaseSourceRelation));
    }

    @Override
    public void insertBatch(List<PurchaseSourceRelation> purchaseSourceRelationList) {
        purchaseSourceRelationMapper.insertBatch(purchaseSourceRelationList.stream().map(PurchaseSourceRelationConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseSourceRelationMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseSourceRelationMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseSourceRelation purchaseSourceRelation) {
        PurchaseSourceRelationPO po = PurchaseSourceRelationConvertor.toPO(purchaseSourceRelation);
        purchaseSourceRelationMapper.update(po);
    }

    @Override
    public PurchaseSourceRelation findById(String corpid, Long id) {
        return PurchaseSourceRelationConvertor.toDomain(purchaseSourceRelationMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseSourceRelation> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseSourceRelationMapper.findByCondition(preparedConditionMap).stream().map(PurchaseSourceRelationConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseSourceRelationMapper.count(preparedConditionMap);
    }
}
