package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;

@Data
public class SupplierInvoiceProfileItemDTO {
    private Long id;
    private String invoiceTitle;
    private String taxNo;
    private String addressPhone;
    private String bankName;
    private String bankAccountNo;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private Integer version;
}
