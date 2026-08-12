package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;

@Component
public class SupplierSaveCommonValidator {
    public void validateForDraft(SupplierSaveDTO dto) {}
    public void validateForSubmit(SupplierSaveDTO dto) {}
}
