package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;

@Component
public class SalesOrderSaveCommonValidator {
    public void validateForDraft(SalesOrderSaveDTO dto) {}
    public void validateForSubmit(SalesOrderSaveDTO dto) {}
}
