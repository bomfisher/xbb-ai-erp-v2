package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSaveDTO;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceSourceTypeEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;
import java.math.BigDecimal;

@Component
public class PurchaseInvoiceSaveBusinessValidator {
    public void validateForSubmit(PurchaseInvoiceSaveDTO dto) {
        if (dto == null || dto.getMain() == null) {
            throw new BizException("采购发票基本信息不能为空");
        }
        if (dto.getSourceSelection() == null || dto.getSourceSelection().getSourceType() == null) {
            throw new BizException("请选择采购发票来源");
        }
        String sourceType = dto.getSourceSelection().getSourceType();
        if (!PurchaseInvoiceSourceTypeEnum.isSupported(sourceType)) {
            throw new BizException("采购发票来源类型不支持");
        }
        if (PurchaseInvoiceSourceTypeEnum.MANUAL.name().equals(sourceType)) {
            if (dto.getSourceSelection().getManualReason() == null
                || dto.getSourceSelection().getManualReason().isBlank()) {
                throw new BizException("手工采购发票必须填写来源原因");
            }
        } else if (dto.getSourceSelection().getSourceId() == null) {
            throw new BizException("请选择来源单据");
        }
        if (dto.getLines() == null || dto.getLines().isEmpty()) {
            throw new BizException("采购发票明细不能为空");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseInvoiceLineDTO line : dto.getLines()) {
            if (line.getProductName() == null || line.getProductName().isBlank()
                || line.getQuantity() == null || line.getQuantity().signum() <= 0
                || line.getUnitPrice() == null || line.getUnitPrice().signum() < 0) {
                throw new BizException("采购发票明细商品、数量和单价不完整");
            }
            if (line.getAmount() != null) {
                total = total.add(line.getAmount());
            }
        }
        if (dto.getMain().getAmount() != null && total.compareTo(dto.getMain().getAmount()) != 0) {
            throw new BizException("采购发票明细合计必须等于含税总金额");
        }
    }
}
