package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSelectQueryDTO extends BaseDTO {
    private String businessCode;
    private String productType;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Long id;
}
