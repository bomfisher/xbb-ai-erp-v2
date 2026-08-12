package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;

@Component
public class PurchaseInboundSaveCommonValidator {
    public void validateForDraft(PurchaseInboundSaveDTO dto) {}
    public void validateForSubmit(PurchaseInboundSaveDTO dto) {}
}
