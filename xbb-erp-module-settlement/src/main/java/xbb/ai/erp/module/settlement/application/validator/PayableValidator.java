package xbb.ai.erp.module.settlement.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;

public final class PayableValidator {

    private PayableValidator() {
    }

    public static void validateSave(PayableSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
