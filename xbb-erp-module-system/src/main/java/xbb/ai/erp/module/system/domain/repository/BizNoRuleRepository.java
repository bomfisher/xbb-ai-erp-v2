package xbb.ai.erp.module.system.domain.repository;

import xbb.ai.erp.module.system.domain.model.BizNoRule;

import java.util.List;

public interface BizNoRuleRepository {

    BizNoRule findRequired(String corpid, String businessCode);

    List<BizNoRule> findAvailable(String corpid);

    void save(BizNoRule rule);
}
