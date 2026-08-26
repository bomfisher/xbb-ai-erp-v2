package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_order")
public class SalesOrderPO extends BaseEntity {
    private String corpid;
    private String orderNo;
    private Long customerId;
    private Long warehouseId;
    private Long orderDate;
    private Long deliveryDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer auditStatus;
    private Integer outboundStatus;
    private Integer receiptStatus;
    private Integer invoiceStatus;
    private String creatorId;
    private String modifyId;
}
