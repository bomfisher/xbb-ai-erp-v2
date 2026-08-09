package xbb.ai.erp.module.demo.sub.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoSubSaveDTO extends BaseDTO {
  private DemoSubMainDTO main;
}
