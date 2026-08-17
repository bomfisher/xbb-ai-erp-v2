package xbb.ai.erp.module.system.domain.model;

import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;

public record BizNoRule(String corpid, String businessCode, String prefix, BizNoRuleTypeEnum ruleType) {
}
