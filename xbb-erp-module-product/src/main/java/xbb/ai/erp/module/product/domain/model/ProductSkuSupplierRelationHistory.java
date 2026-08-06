package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductSkuSupplierRelationHistory {

    private Long id;
    private String corpid;
    private Long relationId;
    private String operateType;
    private String operatorId;
    private String changeSnapshot;
    private String remark;
    private Integer del;
    private String creatorId;
    private String modifyId;
    private Long addTime;
    private Long updateTime;
}
