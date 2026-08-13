package xbb.ai.erp.module.inventory.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_reservation")
public class StockReservationPO extends BaseEntity {
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private String sourceType;
    private Long sourceId;
    private Long sourceLineId;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal outboundQty;
    private java.math.BigDecimal releasedQty;
    private java.math.BigDecimal remainingQty;
    private String status;
    private java.time.LocalDateTime reservedAt;
    private java.time.LocalDateTime releasedAt;
    private String idempotencyKey;
    private Integer version;
    private String creatorId;
    private String modifyId;
}
