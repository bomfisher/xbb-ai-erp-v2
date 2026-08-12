package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;

@Component
public class CustomerSaveProtocolValidator {
    public void validate(CustomerSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
