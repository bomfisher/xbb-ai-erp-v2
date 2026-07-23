package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductUnitVO {

    private Long id;
    private String unitCode;
    private String unitName;
    private Integer precisionNum;
    private Integer enableStatus;
}
