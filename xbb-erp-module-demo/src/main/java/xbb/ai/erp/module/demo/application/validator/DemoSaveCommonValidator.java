package xbb.ai.erp.module.demo.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.base.common.exception.BizException;

@Component
public class DemoSaveCommonValidator {
    public void validateForDraft(DemoSaveDTO dto) {}
    public void validateForSubmit(DemoSaveDTO dto) {
        validateItems(dto.getItems(), "demo子档");
        validateItems(dto.getItems2(), "demo子档2");
    }

    private static void validateItems(java.util.List<DemoItemDTO> items, String itemName) {
        if (items == null) return;
        for (DemoItemDTO item : items) {
            if (item == null || item.getName() == null || item.getName().isBlank()) {
                throw new BizException(itemName + "名称不能为空");
            }
        }
    }
}
