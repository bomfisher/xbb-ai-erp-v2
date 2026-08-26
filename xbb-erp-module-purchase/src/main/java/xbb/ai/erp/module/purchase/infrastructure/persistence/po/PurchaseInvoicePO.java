package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_invoice")
public class PurchaseInvoicePO extends BaseEntity {
    private String corpid;
    private String invoiceNo;
    private String supplierInvoiceNo;
    private Long supplierId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal payableOpenedAmount;
    private java.math.BigDecimal payableAvailableAmount;
    private String invoiceType;
    private String status;
    private Integer auditStatus;
    private Long auditTime;
    private Long postedTime;
    private Long originalInvoiceId;
    private String remark;
    private String sourceType;
    private Long sourceId;
    private String manualReason;
    private String creatorId;
    private String modifyId;
}
