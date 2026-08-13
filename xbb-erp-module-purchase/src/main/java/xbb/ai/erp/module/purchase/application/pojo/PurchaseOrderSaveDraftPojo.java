package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemDTO;

@Data
public class PurchaseOrderSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseOrderMainDTO main;
    private List<PurchaseOrderItemDTO> items;
    private Long updatedTime;
}
