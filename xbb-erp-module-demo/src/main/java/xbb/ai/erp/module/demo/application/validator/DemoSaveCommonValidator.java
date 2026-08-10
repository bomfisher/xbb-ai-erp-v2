package xbb.ai.erp.module.demo.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;

@Component
public class DemoSaveCommonValidator {
    public void validateForDraft(DemoSaveDTO dto) {}
    public void validateForSubmit(DemoSaveDTO dto) {}
}
