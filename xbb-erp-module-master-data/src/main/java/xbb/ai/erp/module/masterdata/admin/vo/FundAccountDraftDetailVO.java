package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountMainDTO;

@Data
public class FundAccountDraftDetailVO {
    private String draftCode;
    private FundAccountMainDTO main;
}
