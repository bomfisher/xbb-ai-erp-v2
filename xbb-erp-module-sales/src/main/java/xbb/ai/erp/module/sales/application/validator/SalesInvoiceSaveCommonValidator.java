package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.sales.admin.SalesInvoiceFieldEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSaveDTO;

@Component
public class SalesInvoiceSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(SalesInvoiceSaveDTO dto) {}
    public void validateForSubmit(SalesInvoiceSaveDTO dto) {
        fieldValueValidator.validate(SalesInvoiceFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
