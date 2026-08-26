package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_invoice_line_source")
public class PurchaseInvoiceLineSourcePO extends BaseEntity {
    private String corpid;
    private Long purchaseInvoiceLineId;
    private String sourceType;
    private Long sourceId;
    private Long sourceLineId;
    private BigDecimal quantity;
    private BigDecimal untaxedAmount;
    private BigDecimal taxAmount;
    private BigDecimal amount;
    private String creatorId;
    private String modifyId;
}
