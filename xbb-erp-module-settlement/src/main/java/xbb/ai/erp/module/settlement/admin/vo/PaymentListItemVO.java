package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class PaymentListItemVO {
    private String id;
    private String paymentNo;
    private String supplierId;
    private String paymentDate;
    private String amount;
    private String writtenOffAmount;
    private String remainingAmount;
    private String paymentType;
    private String paymentMethod;
    private String status;
    private String remark;
}
