package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_outbound_item")
public class SalesOutboundItemPO extends BaseEntity {
    private String corpid;
    private Long salesOutboundId;
    private Long salesOrderItemId;
    private Long skuId;
    private String skuName;
    private Long warehouseId;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal costUnit;
    private java.math.BigDecimal costAmount;
    private Integer outboundStatus;
    private String creatorId;
    private String modifyId;
}
