package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseOrderSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
    private List<PurchaseOrderItemMainDTO> items = new ArrayList<>();
    private PurchaseOrderSectionStatePojo sectionState = new PurchaseOrderSectionStatePojo();
    private Long updatedTime;
}
