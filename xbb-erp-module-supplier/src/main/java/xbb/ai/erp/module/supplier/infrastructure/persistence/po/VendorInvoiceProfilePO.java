package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vendor_invoice_profile")
public class VendorInvoiceProfilePO {
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
