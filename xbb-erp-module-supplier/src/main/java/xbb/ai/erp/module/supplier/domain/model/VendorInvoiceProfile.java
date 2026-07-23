package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class VendorInvoiceProfile {
    private Long id;
    private String corpid;
    private Long vendorId;
    private String invoiceTitle;
    private String taxNo;
    private String registeredAddress;
    private String registeredPhone;
    private String bankName;
    private String bankAccountNo;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
