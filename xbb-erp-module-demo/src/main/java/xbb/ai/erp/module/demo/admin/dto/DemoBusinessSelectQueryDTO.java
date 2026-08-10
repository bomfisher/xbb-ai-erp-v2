package xbb.ai.erp.module.demo.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoBusinessSelectQueryDTO extends BaseDTO {
  private String keyword;
  private Integer pageNum;
  private Integer pageSize;
  private Long id;
}
