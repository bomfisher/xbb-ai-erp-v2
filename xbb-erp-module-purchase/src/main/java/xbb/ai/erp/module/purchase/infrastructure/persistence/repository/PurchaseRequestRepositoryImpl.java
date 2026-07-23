package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseRequestConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseRequestMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchaseRequestRepositoryImpl implements PurchaseRequestRepository {

    private final PurchaseRequestMapper purchaseRequestMapper;

    @Override
    public void insert(PurchaseRequest purchaseRequest) {
        purchaseRequestMapper.insert(PurchaseRequestConvertor.toPO(purchaseRequest));
    }

    @Override
    public void insertBatch(List<PurchaseRequest> purchaseRequestList) {
        purchaseRequestMapper.insertBatch(purchaseRequestList.stream().map(PurchaseRequestConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseRequestMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseRequestMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseRequest purchaseRequest) {
        PurchaseRequestPO po = PurchaseRequestConvertor.toPO(purchaseRequest);
        purchaseRequestMapper.update(po);
    }

    @Override
    public PurchaseRequest findById(String corpid, Long id) {
        return PurchaseRequestConvertor.toDomain(purchaseRequestMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseRequest> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseRequestMapper.findByCondition(preparedConditionMap).stream().map(PurchaseRequestConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseRequestMapper.count(preparedConditionMap);
    }
}
