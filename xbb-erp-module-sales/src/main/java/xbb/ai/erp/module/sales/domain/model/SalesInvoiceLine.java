package xbb.ai.erp.module.sales.domain.model;

import lombok.Data;

@Data
public class SalesInvoiceLine {
    private Long id;
    private String corpid;
    private Long salesInvoiceId;
    private Integer lineNo;
    private Long productId;
    private String productName;
    private String specification;
    private Long unitId;
    private java.math.BigDecimal quantity;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private String remark;
    private String creatorId;
    private String modifyId;
}
