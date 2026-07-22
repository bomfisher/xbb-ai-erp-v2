package xbb.ai.erp.base.common.dto;

import lombok.Data;

@Data
public class ListBaseDTO extends BaseDTO {
    private Integer pageSize;

    private Integer pageNum;
}
