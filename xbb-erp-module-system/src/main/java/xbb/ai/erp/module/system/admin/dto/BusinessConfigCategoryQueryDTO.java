package xbb.ai.erp.module.system.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessConfigCategoryQueryDTO extends BaseDTO {

    private String categoryCode;
}
