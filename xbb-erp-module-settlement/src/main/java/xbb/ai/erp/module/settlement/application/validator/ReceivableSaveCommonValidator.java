package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.settlement.admin.ReceivableFieldEnum;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;

@Component
public class ReceivableSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(ReceivableSaveDTO dto) {}
    public void validateForSubmit(ReceivableSaveDTO dto) {
        fieldValueValidator.validate(ReceivableFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
