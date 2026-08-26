package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.masterdata.admin.FundAccountFieldEnum;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSaveDTO;

@Component
public class FundAccountSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(FundAccountSaveDTO dto) {}
    public void validateForSubmit(FundAccountSaveDTO dto) {
        fieldValueValidator.validate(FundAccountFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
