package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_order_item")
public class SalesOrderItemPO extends BaseEntity {
    private String corpid;
    private Long salesOrderId;
    private Long warehouseId;
    private Integer lineNo;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal deliveredQty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal amount;
    private Integer outboundStatus;
    private String creatorId;
    private String modifyId;
}
