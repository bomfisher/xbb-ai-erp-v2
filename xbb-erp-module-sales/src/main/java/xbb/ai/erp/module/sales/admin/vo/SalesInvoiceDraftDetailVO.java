package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;

@Data
public class SalesInvoiceDraftDetailVO {
    private String draftCode;
    private SalesInvoiceMainDTO main;
}
