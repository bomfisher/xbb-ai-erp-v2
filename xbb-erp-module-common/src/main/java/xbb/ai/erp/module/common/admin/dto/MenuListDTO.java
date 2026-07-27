package xbb.ai.erp.module.common.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuListDTO extends BaseDTO {
    private String surface;
}
