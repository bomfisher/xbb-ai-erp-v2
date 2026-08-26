package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableMainDTO;

@Data
public class ReceivableSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private ReceivableMainDTO main;
    private Long updatedTime;
}
