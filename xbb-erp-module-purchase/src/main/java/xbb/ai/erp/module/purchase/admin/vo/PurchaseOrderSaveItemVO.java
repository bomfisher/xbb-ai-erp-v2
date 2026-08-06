package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseOrderSaveItemVO {
    private PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
    private List<PurchaseOrderItemMainDTO> items = new ArrayList<>();
    private PurchaseOrderSectionStateVO sectionState = new PurchaseOrderSectionStateVO();
}
