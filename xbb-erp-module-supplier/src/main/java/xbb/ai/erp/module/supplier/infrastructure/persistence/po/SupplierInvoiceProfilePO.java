package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier_invoice_profile")
public class SupplierInvoiceProfilePO {
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
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
