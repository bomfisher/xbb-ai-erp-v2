package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

@Data
public class ProductUnitMainDTO {

    private Long id;
    private String corpid;
    private String unitCode;
    private String unitName;
    private Integer precisionNum;
    private Integer enableStatus;
}
