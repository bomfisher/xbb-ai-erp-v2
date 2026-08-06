package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequestSaveExtPojo {
    private List<PurchaseRequestItemMainDTO> items = new ArrayList<>();
}
