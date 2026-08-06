package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_pending_task")
public class PurchasePendingTaskPO extends BaseEntity {
    private String corpid;
    private Long purchaseOrgId;
    private String taskNo;
    private String sourceType;
    private Long sourceDocId;
    private Long sourceLineId;
    private String sourceDocNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private java.math.BigDecimal needQty;
    private java.math.BigDecimal occupiedQty;
    private java.math.BigDecimal generatedRequestQty;
    private java.math.BigDecimal generatedOrderQty;
    private java.math.BigDecimal closedQty;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private Integer priorityLevel;
    private String taskStatus;
    private Integer salesLinkedFlag;
    private Integer version;
    private String creatorId;
    private String modifyId;
}
