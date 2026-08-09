package xbb.ai.erp.module.demo.sub.admin.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoSubListDTO extends BaseDTO {
  private Long id;
  private Long dataId;
  private String name;
  private Integer pageNum;
  private Integer pageSize;
  private Integer offset;
  private String groupByStr;
  private String orderByStr;
  private List<ListFilterCondition> conditions;
}
