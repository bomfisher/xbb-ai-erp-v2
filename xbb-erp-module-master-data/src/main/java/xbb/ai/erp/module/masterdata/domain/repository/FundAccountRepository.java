package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.FundAccount;

import java.util.List;
import java.util.Map;

public interface FundAccountRepository {
    Long insert(FundAccount fundAccount);

    void insertBatch(List<FundAccount> fundAccountList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(FundAccount fundAccount);

    FundAccount findById(String corpid, Long id);

    List<FundAccount> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
