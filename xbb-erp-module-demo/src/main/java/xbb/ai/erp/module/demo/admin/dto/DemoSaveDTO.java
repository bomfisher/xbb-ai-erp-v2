package xbb.ai.erp.module.demo.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoSaveDTO extends BaseDTO {
    private DemoMainDTO main;
    private java.util.List<DemoItemDTO> items;
}
