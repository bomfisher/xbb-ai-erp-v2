package xbb.ai.erp.module.system.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;
import xbb.ai.erp.module.system.infrastructure.persistence.mapper.BizNoRuleMapper;
import xbb.ai.erp.module.system.infrastructure.persistence.po.BizNoRulePO;

@Repository
@RequiredArgsConstructor
public class BizNoRuleRepositoryImpl implements BizNoRuleRepository {

    private final BizNoRuleMapper mapper;

    @Override
    public BizNoRule findRequired(String corpid, String businessCode) {
        BizNoRulePO po = mapper.findByKey(corpid, businessCode);
        if (po == null) {
            throw new BizException("未配置业务编号规则: " + businessCode);
        }
        return new BizNoRule(po.getCorpid(), po.getBusinessCode(), po.getPrefix(), BizNoRuleTypeEnum.valueOf(po.getRuleType()));
    }

    @Override
    public void save(BizNoRule rule) {
        BizNoRulePO po = mapper.findOwnByKey(rule.corpid(), rule.businessCode());
        long now = System.currentTimeMillis();
        if (po == null) {
            po = new BizNoRulePO();
            po.setCorpid(rule.corpid());
            po.setBusinessCode(rule.businessCode());
            po.setAddTime(now);
            po.setDel(0);
            po.setRuleType(rule.ruleType().name());
            po.setPrefix(rule.prefix());
            po.setUpdateTime(now);
            mapper.insert(po);
            return;
        }
        po.setPrefix(rule.prefix());
        po.setRuleType(rule.ruleType().name());
        po.setUpdateTime(now);
        mapper.update(po);
    }
}
