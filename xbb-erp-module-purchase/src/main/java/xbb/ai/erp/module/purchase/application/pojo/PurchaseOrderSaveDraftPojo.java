package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;

@Data
public class PurchaseOrderSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseOrderMainDTO main;
    private Long updatedTime;
}
