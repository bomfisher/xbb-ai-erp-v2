package xbb.ai.erp.module.common.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BusinessBaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListQueryDTO extends BusinessBaseDTO {
    private String keyword;
    private List<ListFilterCondition> conditions;
    private Integer pageNum;
    private Integer pageSize;
}
