package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;

@Data
public class SalesOrderListItemVO {
    private String id;
    private String orderNo;
    private String customerId;
    private String orderDate;
    private String deliveryDate;
    private String totalAmount;
    private Integer status;
    private String remark;
    private String auditStatus;
    private String outboundStatus;
    private String receiptStatus;
    private String creatorId;
    private String modifyId;
}
