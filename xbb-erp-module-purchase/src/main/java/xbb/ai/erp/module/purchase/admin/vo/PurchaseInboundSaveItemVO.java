package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;

@Data
public class PurchaseInboundSaveItemVO {
    private PurchaseInboundMainDTO main;
    private List<PurchaseInboundItemDTO> items;
}
