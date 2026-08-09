package xbb.ai.erp.base.common.dto;

import lombok.Data;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;
import java.util.Objects;

@Data
public class ListBaseDTO extends BaseDTO {
    private Integer pageSize;

    private Integer pageNum;

    private String keyword;

    private List<ListFilterCondition> conditions;

    public Integer getPageSize() {
        return Objects.isNull(pageSize) ? 20 : pageSize;
    }

    public Integer getPageNum() {
        return Objects.isNull(pageNum) ? 1 : pageNum;
    }

    public Integer getOffset() {
        return getPageNum() - 1;
    }
}
