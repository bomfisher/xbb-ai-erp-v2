package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_order")
public class PurchaseOrderPO extends BaseEntity {
    private String corpid;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private Integer auditStatus;
    private Integer inboundStatus;
    private Integer paymentStatus;
    private Integer invoiceStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
}
