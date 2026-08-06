package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseRequestDraftListItemVO {
    private String draftCode;
    private String draftTitle;
    private String requestNo;
    private String applicantId;
    private Long updatedTime;
}
