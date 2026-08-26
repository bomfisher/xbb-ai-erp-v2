package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.PaymentMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentInfoDTO;
import java.util.List;

@Data
public class PaymentDraftDetailVO {
    private String draftCode;
    private PaymentMainDTO main;
    private List<PaymentInfoDTO> paymentInfos;
}
