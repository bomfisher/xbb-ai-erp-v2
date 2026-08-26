package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.PaymentMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentInfoDTO;

@Data
public class PaymentSaveItemVO {
    private PaymentMainDTO main;
    private java.util.List<PaymentInfoDTO> paymentInfos;
}
