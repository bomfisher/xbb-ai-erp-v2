package xbb.ai.erp.module.purchase.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;

public final class PurchaseInboundValidator {

    private PurchaseInboundValidator() {
    }

    public static void validateSave(PurchaseInboundSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
