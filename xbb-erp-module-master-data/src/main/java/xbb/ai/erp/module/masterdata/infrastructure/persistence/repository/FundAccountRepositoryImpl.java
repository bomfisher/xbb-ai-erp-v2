package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.domain.repository.FundAccountRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.FundAccountConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.FundAccountMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.FundAccountPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataFundAccountRepositoryImpl")
@RequiredArgsConstructor
public class FundAccountRepositoryImpl implements FundAccountRepository {

    private final FundAccountMapper fundAccountMapper;

    @Override
    public Long insert(FundAccount fundAccount) {
        FundAccountPO po = FundAccountConvertor.toPO(fundAccount);
        initializeForInsert(po);
        fundAccountMapper.insert(po);
        fundAccount.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<FundAccount> fundAccountList) {
        List<FundAccountPO> poList = fundAccountList.stream().map(FundAccountConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        fundAccountMapper.insertBatch(poList);
        for (int index = 0; index < fundAccountList.size(); index++) {
            fundAccountList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        fundAccountMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        fundAccountMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(FundAccount fundAccount) {
        FundAccountPO po = FundAccountConvertor.toPO(fundAccount);
        fundAccountMapper.update(po);
    }

    @Override
    public FundAccount findById(String corpid, Long id) {
        return FundAccountConvertor.toDomain(fundAccountMapper.findById(corpid, id));
    }

    @Override
    public List<FundAccount> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return fundAccountMapper.findByCondition(preparedConditionMap).stream().map(FundAccountConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return fundAccountMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
