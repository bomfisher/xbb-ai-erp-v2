package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequestSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    private List<PurchaseRequestItemMainDTO> items = new ArrayList<>();
    private PurchaseRequestSectionStatePojo sectionState = new PurchaseRequestSectionStatePojo();
    private Long updatedTime;
}
