package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.PayableMainDTO;

@Data
public class PayableDraftDetailVO {
    private String draftCode;
    private PayableMainDTO main;
}
