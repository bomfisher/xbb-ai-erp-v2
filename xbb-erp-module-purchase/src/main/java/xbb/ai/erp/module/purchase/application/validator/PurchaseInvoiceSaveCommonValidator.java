package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceFieldEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSaveDTO;

@Component
public class PurchaseInvoiceSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(PurchaseInvoiceSaveDTO dto) {}
    public void validateForSubmit(PurchaseInvoiceSaveDTO dto) {
        fieldValueValidator.validate(PurchaseInvoiceFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
