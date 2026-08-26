package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.PayableMainDTO;

@Data
public class PayableSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PayableMainDTO main;
    private Long updatedTime;
}
