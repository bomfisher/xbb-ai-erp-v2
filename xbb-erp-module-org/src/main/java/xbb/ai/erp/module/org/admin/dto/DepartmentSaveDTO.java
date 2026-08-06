package xbb.ai.erp.module.org.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentSaveDTO extends BaseDTO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private Long parentId;
    private Integer enableStatus;
}
