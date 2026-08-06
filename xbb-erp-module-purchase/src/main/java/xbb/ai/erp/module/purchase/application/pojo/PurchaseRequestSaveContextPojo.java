package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;

@Data
public class PurchaseRequestSaveContextPojo {
    private String corpid;
    private PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    private PurchaseRequestSaveExtPojo ext = new PurchaseRequestSaveExtPojo();
    private PurchaseRequestSectionStatePojo sectionState = new PurchaseRequestSectionStatePojo();
    private PurchaseRequestDraftMetaPojo draftMeta = new PurchaseRequestDraftMetaPojo();
    private Integer submitMode;
}
