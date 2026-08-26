package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.settlement.admin.PaymentFieldEnum;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;

@Component
public class PaymentSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(PaymentSaveDTO dto) {}
    public void validateForSubmit(PaymentSaveDTO dto) {
        fieldValueValidator.validate(PaymentFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
