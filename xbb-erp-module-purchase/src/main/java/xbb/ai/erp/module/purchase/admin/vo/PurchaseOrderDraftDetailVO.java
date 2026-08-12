package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;

@Data
public class PurchaseOrderDraftDetailVO {
    private String draftCode;
    private PurchaseOrderMainDTO main;
}
