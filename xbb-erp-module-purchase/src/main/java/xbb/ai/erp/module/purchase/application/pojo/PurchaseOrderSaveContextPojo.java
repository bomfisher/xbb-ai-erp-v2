package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;

@Data
public class PurchaseOrderSaveContextPojo {
    private String corpid;
    private PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
    private PurchaseOrderSaveExtPojo ext = new PurchaseOrderSaveExtPojo();
    private PurchaseOrderSectionStatePojo sectionState = new PurchaseOrderSectionStatePojo();
    private PurchaseOrderDraftMetaPojo draftMeta = new PurchaseOrderDraftMetaPojo();
    private Integer submitMode;
}
