package xbb.ai.erp.base.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatchBaseDTO extends BaseDTO{
    private List<Long> idList;
}
