package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;

@Data
public class SalesOrderDraftDetailVO {
    private String draftCode;
    private SalesOrderMainDTO main;
}
