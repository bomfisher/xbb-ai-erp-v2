package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListDTO extends BaseDTO {
    private Long id;
    private String customerCode;
    private String customerName;
    private Integer enabled;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
