package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.sales.admin.SalesOrderFieldEnum;
import xbb.ai.erp.module.sales.admin.SalesOutboundFieldEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;

@Component
public class SalesOutboundSaveCommonValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(SalesOutboundSaveDTO dto) {}
    public void validateForSubmit(SalesOutboundSaveDTO dto) {
        fieldValueValidator.validate(SalesOutboundFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);

    }
}
