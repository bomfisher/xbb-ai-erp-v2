package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;

@Data
public class SalesInvoiceSourceDocumentVO {
    private Long id;
    private String documentNo;
    private Long customerId;
    private String sourceType;
}
