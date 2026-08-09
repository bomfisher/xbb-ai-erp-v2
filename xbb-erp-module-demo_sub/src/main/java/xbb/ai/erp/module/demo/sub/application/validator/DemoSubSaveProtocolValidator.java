package xbb.ai.erp.module.demo.sub.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;

public class DemoSubSaveProtocolValidator {
  public void validate(DemoSubSaveDTO dto) {
    if (dto == null || dto.getMain() == null) throw new BizException("DEMO_SUB 主档不能为空");
    if (dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("公司不能为空");
  }
}
