package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseOrderSaveExtPojo {
    private List<PurchaseOrderItemMainDTO> items = new ArrayList<>();
}
