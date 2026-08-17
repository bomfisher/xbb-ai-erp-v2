package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;

@Data
public class SalesOutboundListItemVO {
    private String id;
    private String outboundNo;
    private String salesOrderId;
    private String customerId;
    private String customerName;
    private String warehouseId;
    private String outboundDate;
    private String totalAmount;
    private String status;
    private String remark;
    private String auditStatus;
    private String creatorId;
    private String modifyId;
}
