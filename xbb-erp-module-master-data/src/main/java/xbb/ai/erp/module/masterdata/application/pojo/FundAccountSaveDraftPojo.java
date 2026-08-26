package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountMainDTO;

@Data
public class FundAccountSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private FundAccountMainDTO main;
    private Long updatedTime;
}
