package xbb.ai.erp.module.settlement.application.validator;

import java.math.BigDecimal;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;

public final class ReceivableValidator {

    private ReceivableValidator() {
    }

    public static void validateSave(ReceivableSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
        if (dto.getMain().getAmount() == null || dto.getMain().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("初始应收金额必须大于零");
        }
    }
}
