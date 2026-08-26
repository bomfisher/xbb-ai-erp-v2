package xbb.ai.erp.module.settlement.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;

public final class ReceiptValidator {

    private ReceiptValidator() {
    }

    public static void validateSave(ReceiptSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
