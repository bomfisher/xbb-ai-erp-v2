package xbb.ai.erp.module.sales.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;

@Component
public class SalesOutboundSaveCommonValidator {
    public void validateForDraft(SalesOutboundSaveDTO dto) {}
    public void validateForSubmit(SalesOutboundSaveDTO dto) {}
}
