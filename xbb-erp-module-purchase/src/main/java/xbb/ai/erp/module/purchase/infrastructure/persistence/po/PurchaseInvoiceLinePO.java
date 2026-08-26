package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_invoice_line")
public class PurchaseInvoiceLinePO extends BaseEntity {
    private String corpid;
    private Long purchaseInvoiceId;
    private Integer lineNo;
    private Long productId;
    private String productName;
    private String specification;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal untaxedAmount;
    private BigDecimal taxAmount;
    private BigDecimal amount;
    private String remark;
    private String creatorId;
    private String modifyId;
}
