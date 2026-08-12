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
    private String inboundNo;
    private Long purchaseOrderId;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private Long inboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
