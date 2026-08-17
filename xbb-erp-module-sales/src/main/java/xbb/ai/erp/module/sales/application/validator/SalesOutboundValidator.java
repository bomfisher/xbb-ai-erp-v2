package xbb.ai.erp.module.sales.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;

public final class SalesOutboundValidator {

    private SalesOutboundValidator() {
    }

    public static void validateSave(SalesOutboundSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
