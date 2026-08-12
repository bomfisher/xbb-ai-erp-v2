package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;

@Component
public class PurchaseInboundSaveProtocolValidator {
    public void validate(PurchaseInboundSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
