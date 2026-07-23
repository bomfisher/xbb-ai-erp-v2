package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductUnit {

    private Long id;
    private String corpid;
    private String unitCode;
    private String unitName;
    private Integer precisionNum;
    private Integer enableStatus;
}
