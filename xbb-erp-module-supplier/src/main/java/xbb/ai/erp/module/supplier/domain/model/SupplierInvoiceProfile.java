package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class SupplierInvoiceProfile {
    private Long id;
    private String corpid;
    private Long supplierId;
    private String invoiceTitle;
    private String taxNo;
    private String addressPhone;
    private String bankName;
    private String bankAccountNo;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
