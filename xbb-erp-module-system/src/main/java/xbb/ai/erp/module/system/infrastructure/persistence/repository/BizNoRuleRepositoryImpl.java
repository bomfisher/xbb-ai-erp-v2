package xbb.ai.erp.module.system.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.model.BizNoSerialModeEnum;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;
import xbb.ai.erp.module.system.infrastructure.persistence.mapper.BizNoRuleMapper;
import xbb.ai.erp.module.system.infrastructure.persistence.po.BizNoRulePO;

import java.util.List;

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
        return toDomain(po);
    }

    @Override
    public List<BizNoRule> findAvailable(String corpid) {
        return mapper.findAvailableByCorpid(corpid).stream()
            .map(this::toDomain)
            .toList();
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
        }
        po.setPrefix(rule.prefix());
        po.setIncludeDate(rule.includeDate());
        po.setSuffixLength(rule.suffixLength());
        po.setSerialMode(rule.serialMode().name());
        po.setRuleType(rule.ruleType().name());
        po.setUpdateTime(now);
        if (po.getId() == null) {
            mapper.insert(po);
        } else {
            mapper.update(po);
        }
    }

    private BizNoRule toDomain(BizNoRulePO po) {
        BizNoRuleTypeEnum ruleType = BizNoRuleTypeEnum.valueOf(po.getRuleType());
        Integer includeDate = po.getIncludeDate() == null && ruleType == BizNoRuleTypeEnum.DOCUMENT ? 1 : po.getIncludeDate();
        Integer suffixLength = po.getSuffixLength() == null ? 5 : po.getSuffixLength();
        BizNoSerialModeEnum serialMode = po.getSerialMode() == null
            ? ruleType == BizNoRuleTypeEnum.DOCUMENT ? BizNoSerialModeEnum.DAILY : BizNoSerialModeEnum.CONTINUOUS
            : BizNoSerialModeEnum.valueOf(po.getSerialMode());
        return new BizNoRule(po.getCorpid(), po.getBusinessCode(), po.getPrefix(), includeDate, suffixLength, serialMode, ruleType);
    }
}
