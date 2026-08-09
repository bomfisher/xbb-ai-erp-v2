package xbb.ai.erp.module.org.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentSelectQueryDTO extends ListBaseDTO {
    private Long id;
    private String keyword;
}
