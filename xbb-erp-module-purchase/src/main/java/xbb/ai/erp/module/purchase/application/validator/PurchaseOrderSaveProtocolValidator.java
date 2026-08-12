package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;

@Component
public class PurchaseOrderSaveProtocolValidator {
    public void validate(PurchaseOrderSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
