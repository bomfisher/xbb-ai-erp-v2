package xbb.ai.erp.module.org.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeBatchDTO extends BaseDTO {
    private List<String> idList;
}
