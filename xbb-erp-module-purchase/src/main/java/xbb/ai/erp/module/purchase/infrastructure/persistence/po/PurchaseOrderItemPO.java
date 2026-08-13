package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_order_item")
public class PurchaseOrderItemPO extends BaseEntity {
    private String corpid;
    private Long purchaseOrderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal inboundQty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal amount;
    private String creatorId;
    private String modifyId;
}
