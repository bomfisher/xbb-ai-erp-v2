package xbb.ai.erp.module.masterdata.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;

public final class WarehouseValidator {

    private WarehouseValidator() {
    }

    public static void validateSave(WarehouseSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
