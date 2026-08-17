package xbb.ai.erp.module.sales.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;

@Data
public class SalesOrderSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private SalesOrderMainDTO main;
    private Long updatedTime;
}
