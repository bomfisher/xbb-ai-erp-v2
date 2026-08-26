package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceListDTO extends BaseDTO {
    private Long id;
    private String invoiceNo;
    private String supplierInvoiceNo;
    private Long supplierId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private String invoiceType;
    private String status;
    private Integer auditStatus;
    private Long originalInvoiceId;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
