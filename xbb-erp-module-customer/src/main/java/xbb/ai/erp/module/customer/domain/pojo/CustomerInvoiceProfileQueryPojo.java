package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerInvoiceProfileQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String invoiceTitle;
    private String taxNo;
    private String bizStatus;
    private Integer defaultFlag;
}
