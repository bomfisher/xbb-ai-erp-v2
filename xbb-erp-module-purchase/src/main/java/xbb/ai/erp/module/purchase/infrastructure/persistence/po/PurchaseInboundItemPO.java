package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_inbound_item")
public class PurchaseInboundItemPO extends BaseEntity {
    private String corpid;
    private Long purchaseInboundId;
    private Long purchaseOrderItemId;
    private Long skuId;
    private String skuName;
    private String unitName;
    private Long warehouseId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal costUnit;
    private java.math.BigDecimal costAmount;
    private String creatorId;
    private String modifyId;
}
