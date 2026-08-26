package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.PaymentMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentInfoDTO;
import java.util.List;

@Data
public class PaymentSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PaymentMainDTO main;
    private List<PaymentInfoDTO> paymentInfos;
    private Long updatedTime;
}
