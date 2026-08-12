package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;

@Data
public class PurchaseInboundDraftDetailVO {
    private String draftCode;
    private PurchaseInboundMainDTO main;
}
