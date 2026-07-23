package xbb.ai.erp.module.product.infrastructure.persistence.po;

import lombok.Data;

@Data
public class ProductCategoryPO {

    private Long id;
    private String corpid;
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer categoryLevel;
    private Integer sortNo;
    private Integer enableStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
