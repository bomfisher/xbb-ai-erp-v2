package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;

@Component
public class WarehouseSaveCommonValidator {
    public void validateForDraft(WarehouseSaveDTO dto) {}
    public void validateForSubmit(WarehouseSaveDTO dto) {}
}
