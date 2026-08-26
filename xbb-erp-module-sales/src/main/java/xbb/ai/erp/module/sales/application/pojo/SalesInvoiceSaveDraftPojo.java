package xbb.ai.erp.module.sales.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;

@Data
public class SalesInvoiceSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private SalesInvoiceMainDTO main;
    private Long updatedTime;
}
