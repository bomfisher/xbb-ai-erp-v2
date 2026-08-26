package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.sales.admin.SalesOrderFieldEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;

@Component
public class SalesOrderSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(SalesOrderSaveDTO dto) {}
    public void validateForSubmit(SalesOrderSaveDTO dto) {
        fieldValueValidator.validate(SalesOrderFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
