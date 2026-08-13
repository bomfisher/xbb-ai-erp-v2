package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;

@Data
public class PurchaseInboundSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseInboundMainDTO main;
    private List<PurchaseInboundItemDTO> items;
    private Long updatedTime;
}
