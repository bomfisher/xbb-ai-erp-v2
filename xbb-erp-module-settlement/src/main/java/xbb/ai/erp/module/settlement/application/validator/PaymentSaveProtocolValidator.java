package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;

@Component
public class PaymentSaveProtocolValidator {
    public void validate(PaymentSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
