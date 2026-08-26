package xbb.ai.erp.module.settlement.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentInfoDTO;
import java.math.BigDecimal;
import java.util.List;

@Component
public class PaymentSaveBusinessValidator {
    public void validateForSubmit(PaymentSaveDTO dto) {
        if (dto == null || dto.getMain() == null) {
            throw new BizException("付款基本信息不能为空");
        }
        List<PaymentInfoDTO> details = dto.getPaymentInfos();
        if (details == null || details.isEmpty()) {
            throw new BizException("付款信息不能为空");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentInfoDTO detail : details) {
            if (detail.getBankAccountId() == null || detail.getPaymentMethod() == null
                || detail.getPaymentMethod().isBlank()) {
                throw new BizException("付款账户和付款方式不能为空");
            }
            if (detail.getAmount() == null || detail.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BizException("付款明细金额必须大于0");
            }
            total = total.add(detail.getAmount());
        }
        if (dto.getMain().getAmount() == null) {
            dto.getMain().setAmount(total);
        } else if (total.compareTo(dto.getMain().getAmount()) != 0) {
            throw new BizException("付款明细合计必须等于付款金额");
        }
    }
}
