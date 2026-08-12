package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;

@Data
public class PurchaseInboundSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseInboundMainDTO main;
    private Long updatedTime;
}
