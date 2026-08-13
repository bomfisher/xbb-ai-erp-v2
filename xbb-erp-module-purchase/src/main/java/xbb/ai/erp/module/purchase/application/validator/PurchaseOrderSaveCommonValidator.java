package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;

@Component
public class PurchaseOrderSaveCommonValidator {
    public void validateForDraft(PurchaseOrderSaveDTO dto) {}
    public void validateForSubmit(PurchaseOrderSaveDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BizException("采购产品不能为空");
        }
        for (PurchaseOrderItemDTO item : dto.getItems()) {
            if (item == null || item.getSkuId() == null
                || item.getQty() == null || item.getUnitPrice() == null) {
                throw new BizException("采购产品快照、单位、数量和单价不能为空");
            }
            if (item.getQty().compareTo(BigDecimal.ZERO) <= 0 || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new BizException("采购数量必须大于零，单价不能小于零");
            }
        }
    }
}
