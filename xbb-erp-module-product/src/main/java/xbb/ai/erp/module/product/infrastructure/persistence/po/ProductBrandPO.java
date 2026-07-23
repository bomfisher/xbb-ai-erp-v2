package xbb.ai.erp.module.product.infrastructure.persistence.po;

import lombok.Data;

@Data
public class ProductBrandPO {

    private Long id;
    private String corpid;
    private String brandCode;
    private String brandName;
    private Integer sortNo;
    private Integer enableStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
