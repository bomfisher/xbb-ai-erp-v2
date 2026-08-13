package xbb.ai.erp.module.inventory.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_balance")
public class StockBalancePO extends BaseEntity {
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal lockedQty;
    private java.math.BigDecimal availableQty;
    private java.math.BigDecimal totalCost;
    private java.math.BigDecimal unitCost;
    private Integer version;
    private String creatorId;
    private String modifyId;
}
