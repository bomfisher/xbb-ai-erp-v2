package xbb.ai.erp.module.system.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessConfigSaveDTO extends BaseDTO {

    private String businessCode;
    private Map<String, Object> values;
}
