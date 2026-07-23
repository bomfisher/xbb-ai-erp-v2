package xbb.ai.erp.module.product.infrastructure.persistence.po;

import lombok.Data;

@Data
public class ProductUnitPO {

    private Long id;
    private String corpid;
    private String unitCode;
    private String unitName;
    private Integer precisionNum;
    private Integer enableStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
