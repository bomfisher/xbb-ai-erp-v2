package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.settlement.admin.PayableFieldEnum;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;

@Component
public class PayableSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(PayableSaveDTO dto) {}
    public void validateForSubmit(PayableSaveDTO dto) {
        fieldValueValidator.validate(PayableFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
