package xbb.ai.erp.module.demo.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;

public final class DemoValidator {

    private DemoValidator() {
    }

    public static void validateSave(DemoSaveDTO dto) {
        if (dto == null) {
            throw new BizException("save dto不能为空");
        }
        if (dto.getMain() == null) {
            throw new BizException("main不能为空");
        }
    }
}
