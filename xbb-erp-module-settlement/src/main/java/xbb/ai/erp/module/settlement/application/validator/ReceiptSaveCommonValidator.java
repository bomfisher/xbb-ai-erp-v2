package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.settlement.admin.ReceiptFieldEnum;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;

@Component
public class ReceiptSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(ReceiptSaveDTO dto) {}
    public void validateForSubmit(ReceiptSaveDTO dto) {
        fieldValueValidator.validate(ReceiptFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
