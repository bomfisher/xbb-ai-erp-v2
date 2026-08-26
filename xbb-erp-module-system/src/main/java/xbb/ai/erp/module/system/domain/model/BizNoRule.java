package xbb.ai.erp.module.system.domain.model;

import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;

public record BizNoRule(
    String corpid,
    String businessCode,
    String prefix,
    Integer includeDate,
    Integer suffixLength,
    BizNoSerialModeEnum serialMode,
    BizNoRuleTypeEnum ruleType
) {

    public BizNoRule(String corpid, String businessCode, String prefix, BizNoRuleTypeEnum ruleType) {
        this(
            corpid,
            businessCode,
            prefix,
            ruleType == BizNoRuleTypeEnum.DOCUMENT ? 1 : 0,
            5,
            ruleType == BizNoRuleTypeEnum.DOCUMENT ? BizNoSerialModeEnum.DAILY : BizNoSerialModeEnum.CONTINUOUS,
            ruleType
        );
    }
}
