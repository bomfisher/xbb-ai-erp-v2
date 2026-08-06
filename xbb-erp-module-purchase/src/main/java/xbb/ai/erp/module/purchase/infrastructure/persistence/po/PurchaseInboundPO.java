package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_inbound")
public class PurchaseInboundPO extends BaseEntity {
    private String corpid;
    private Long purchaseOrgId;
    private String inboundNo;
    private String sourceDocType;
    private Long sourceDocId;
    private Long vendorId;
    private Long warehouseId;
    private Long actualInboundTime;
    private String bizStatus;
    private String approvalStatus;
    private String executionStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal inventoryCostAmount;
    private String inventoryFlowNo;
    private String payableTriggerStatus;
    private String payableNo;
    private String invoiceSourceStatus;
    private Integer periodLockedFlag;
    private Integer version;
    private String remark;
    private String creatorId;
    private String modifyId;
}
