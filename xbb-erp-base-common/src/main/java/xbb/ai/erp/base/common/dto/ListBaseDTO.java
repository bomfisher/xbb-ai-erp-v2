package xbb.ai.erp.base.common.dto;

import lombok.Data;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
public class ListBaseDTO extends BaseDTO {
    private Integer pageSize;

    private Integer pageNum;

    private String keyword;

    private List<ListFilterCondition> conditions;
}
