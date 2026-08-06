package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;

@Data
public class PurchaseOrderDraftMetaDTO {
    private String draftCode;
    private String draftTitle;
    private Long updatedTime;
}
