package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSaveDTO;

@Component
public class ProductSpuSaveProtocolValidator {
    public void validate(ProductSpuSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
