package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;

@Component
public class SupplierSaveProtocolValidator {
    public void validate(SupplierSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
