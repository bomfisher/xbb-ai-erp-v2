package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequestSaveItemVO {
    private PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    private List<PurchaseRequestItemMainDTO> items = new ArrayList<>();
    private PurchaseRequestSectionStateVO sectionState = new PurchaseRequestSectionStateVO();
}
