package xbb.ai.erp.module.system.domain.repository;

import xbb.ai.erp.module.system.domain.model.BizNoRule;

public interface BizNoRuleRepository {

    BizNoRule findRequired(String corpid, String businessCode);

    void save(BizNoRule rule);
}
