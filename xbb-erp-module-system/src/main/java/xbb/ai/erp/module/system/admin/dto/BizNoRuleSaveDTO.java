package xbb.ai.erp.module.system.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizNoRuleSaveDTO extends BaseDTO {

    private String businessCode;
    private String prefix;
    private Integer includeDate;
    private Integer suffixLength;
    private String serialMode;
    private String ruleType;
}
