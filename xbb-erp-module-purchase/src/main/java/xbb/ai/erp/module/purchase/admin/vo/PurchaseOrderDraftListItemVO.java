package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseOrderDraftListItemVO {
    private String draftCode;
    private String draftTitle;
    private String orderNo;
    private Long vendorId;
    private Long updatedTime;
}
