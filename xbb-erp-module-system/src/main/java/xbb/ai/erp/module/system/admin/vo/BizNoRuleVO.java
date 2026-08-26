package xbb.ai.erp.module.system.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.BaseVO;

@Data
public class BizNoRuleVO extends BaseVO {

    private String businessCode;
    private String prefix;
    private Integer includeDate;
    private Integer suffixLength;
    private String serialMode;
    private String ruleType;
    private Integer overridden;
}
