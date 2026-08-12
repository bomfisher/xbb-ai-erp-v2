package xbb.ai.erp.module.demo.sub.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;

public final class DemoSubValidator {

  private DemoSubValidator() {}

  public static void validateSave(DemoSubSaveDTO dto) {
    if (dto == null) {
      throw new BizException("save dto不能为空");
    }
    if (dto.getMain() == null) {
      throw new BizException("main不能为空");
    }
  }
}
