package xbb.ai.erp.module.masterdata.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;

public final class SupplierValidator {

    private SupplierValidator() {
    }

    public static void validateSave(SupplierSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
