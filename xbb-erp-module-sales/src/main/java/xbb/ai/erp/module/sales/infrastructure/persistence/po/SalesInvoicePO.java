package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_invoice")
public class SalesInvoicePO extends BaseEntity {
    private String corpid;
    private String invoiceNo;
    private Long customerId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal receivableOpenedAmount;
    private java.math.BigDecimal receivableAvailableAmount;
    private String invoiceType;
    private Long originalInvoiceId;
    private String status;
    private Integer auditStatus;
    private Long auditTime;
    private Long postedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
