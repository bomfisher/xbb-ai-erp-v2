package xbb.ai.erp.module.org.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeIdDTO extends BaseDTO {
    private String id;
}
