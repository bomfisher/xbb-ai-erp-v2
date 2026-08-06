package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

@Data
public class ProductBrandMainDTO {

    private Long id;
    private String corpid;
    private String brandCode;
    private String brandName;
    private Integer sortNo;
    private Integer enableStatus;
}
