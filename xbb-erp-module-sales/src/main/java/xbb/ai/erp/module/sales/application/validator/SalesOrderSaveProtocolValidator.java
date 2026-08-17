package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;

@Component
public class SalesOrderSaveProtocolValidator {
    public void validate(SalesOrderSaveDTO dto) { if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException("保存协议不完整"); }
}
