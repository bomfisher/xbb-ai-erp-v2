package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;

@Component
public class PurchaseOrderSaveCommonValidator {
    public void validateForDraft(PurchaseOrderSaveDTO dto) {}
    public void validateForSubmit(PurchaseOrderSaveDTO dto) {}
}
