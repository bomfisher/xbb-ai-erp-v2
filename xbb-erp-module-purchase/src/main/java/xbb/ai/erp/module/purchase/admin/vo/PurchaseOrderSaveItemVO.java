package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemDTO;

@Data
public class PurchaseOrderSaveItemVO {
    private PurchaseOrderMainDTO main;
    private List<PurchaseOrderItemDTO> items;
}
