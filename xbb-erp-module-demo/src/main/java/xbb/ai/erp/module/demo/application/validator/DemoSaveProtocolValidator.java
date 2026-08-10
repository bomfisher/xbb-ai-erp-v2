package xbb.ai.erp.module.demo.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;

@Component
public class DemoSaveProtocolValidator {
    public void validate(DemoSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
