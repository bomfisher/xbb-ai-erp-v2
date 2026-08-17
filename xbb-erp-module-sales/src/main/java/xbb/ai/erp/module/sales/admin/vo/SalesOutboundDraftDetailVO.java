package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundMainDTO;

@Data
public class SalesOutboundDraftDetailVO {
    private String draftCode;
    private SalesOutboundMainDTO main;
}
