package xbb.ai.erp.module.inventory.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_transaction")
public class StockTransactionPO extends BaseEntity {
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private String actionType;
    private java.math.BigDecimal qtyBefore;
    private java.math.BigDecimal qtyChange;
    private java.math.BigDecimal qtyAfter;
    private String sourceType;
    private Long sourceId;
    private String idempotencyKey;
    private String operatorId;
    private java.time.LocalDateTime occurredAt;
    private String creatorId;
    private String modifyId;
}
