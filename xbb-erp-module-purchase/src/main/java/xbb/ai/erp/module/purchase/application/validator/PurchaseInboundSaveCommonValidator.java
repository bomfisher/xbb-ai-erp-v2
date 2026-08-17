package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;

@Component
public class PurchaseInboundSaveCommonValidator {
    public void validateForDraft(PurchaseInboundSaveDTO dto) {}
    public void validateForSubmit(PurchaseInboundSaveDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BizException("入库产品不能为空");
        }
        for (PurchaseInboundItemDTO item : dto.getItems()) {
            if (item == null || item.getPurchaseOrderItemId() == null || item.getSkuId() == null
                || item.getSkuName() == null || item.getSkuName().isBlank() || item.getUnitName() == null || item.getUnitName().isBlank()
                || item.getWarehouseId() == null || item.getQty() == null || item.getUnitPrice() == null) {
//                throw new BizException("入库产品来源、快照、单位、仓库、数量和单价不能为空");
            }
            if (item.getQty().compareTo(BigDecimal.ZERO) <= 0 || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0
                || (item.getCostUnit() != null && item.getCostUnit().compareTo(BigDecimal.ZERO) < 0)) {
                throw new BizException("入库数量必须大于零，单价和成本单价不能小于零");
            }
        }
    }
}
