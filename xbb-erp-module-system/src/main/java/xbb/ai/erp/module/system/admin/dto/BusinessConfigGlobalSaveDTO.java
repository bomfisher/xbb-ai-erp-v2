package xbb.ai.erp.module.system.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessConfigGlobalSaveDTO extends BaseDTO {

    private List<String> autoApprovalBusinessCodes;
}
