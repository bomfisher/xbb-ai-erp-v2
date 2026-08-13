package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;

@Data
public class PurchaseInboundDraftDetailVO {
    private String draftCode;
    private PurchaseInboundMainDTO main;
    private List<PurchaseInboundItemDTO> items;
}
