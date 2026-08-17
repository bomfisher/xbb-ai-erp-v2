package xbb.ai.erp.module.sales.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_outbound")
public class SalesOutboundPO extends BaseEntity {
    private String corpid;
    private String outboundNo;
    private Long salesOrderId;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private Long outboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private Integer auditStatus;
    private String creatorId;
    private String modifyId;
}
