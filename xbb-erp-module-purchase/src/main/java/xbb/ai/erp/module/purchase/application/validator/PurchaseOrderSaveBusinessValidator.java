package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.purchase.admin.PurchaseInboundFieldEnum;
import xbb.ai.erp.module.purchase.admin.PurchaseOrderFieldEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;

@Component
public class PurchaseOrderSaveBusinessValidator {
    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();


    public void validateForSubmit(PurchaseOrderSaveDTO dto) {
        fieldValueValidator.validate(PurchaseOrderFieldEnum.fieldRules(), dto, FieldValidateModeEnum.SUBMIT);
    }
}
