package xbb.ai.erp.module.demo.sub.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;

public class DemoSubSaveCommonValidator {
  public void validateForDraft(DemoSubSaveDTO dto) {}

  public void validateForSubmit(DemoSubSaveDTO dto) {
    if (dto.getMain().getDataId() == null) throw new BizException("关联数据不能为空");
    if (dto.getMain().getName() == null || dto.getMain().getName().isBlank())
      throw new BizException("名称不能为空");
  }
}
