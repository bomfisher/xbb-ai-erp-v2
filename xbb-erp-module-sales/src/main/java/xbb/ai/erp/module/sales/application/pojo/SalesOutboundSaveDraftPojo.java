package xbb.ai.erp.module.sales.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundMainDTO;

@Data
public class SalesOutboundSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private SalesOutboundMainDTO main;
    private Long updatedTime;
}
