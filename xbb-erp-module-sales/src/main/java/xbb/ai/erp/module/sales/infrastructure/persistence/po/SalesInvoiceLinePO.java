package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_invoice_line")
public class SalesInvoiceLinePO extends BaseEntity {
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
