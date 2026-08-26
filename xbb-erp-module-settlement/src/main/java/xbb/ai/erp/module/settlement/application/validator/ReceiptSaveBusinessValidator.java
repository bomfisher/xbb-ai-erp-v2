package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;

@Component
public class ReceiptSaveBusinessValidator {
    public void validateForSubmit(ReceiptSaveDTO dto) {
        List<xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO> payments = dto.getPayments();
        if (payments == null || payments.isEmpty()) {
            throw new BizException("至少需要填写一条收款信息");
        }
        BigDecimal amount = payments.stream()
            .map(xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO::getAmount)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (amount.signum() <= 0) {
            throw new BizException("实际收款金额必须大于零");
        }
        if ("CUSTOMER_PAYMENT".equals(dto.getMain().getReceiptType())) {
            List<ReceiptWriteOffItemDTO> writeOffs = dto.getWriteOffs();
            if (writeOffs == null || writeOffs.isEmpty()) {
                throw new BizException("普通收款至少需要关联一笔应收款");
            }
        }
        if ("ADVANCE_PAYMENT".equals(dto.getMain().getReceiptType())
            && dto.getWriteOffs() != null && !dto.getWriteOffs().isEmpty()) {
            throw new BizException("预收款不能在创建时核销应收款");
        }
    }
}
