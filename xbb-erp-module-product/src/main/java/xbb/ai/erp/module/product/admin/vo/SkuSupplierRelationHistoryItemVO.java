package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class SkuSupplierRelationHistoryItemVO {

    private Long id;
    private String operateType;
    private String operatorId;
    private Long operateTime;
    private String remark;
}
