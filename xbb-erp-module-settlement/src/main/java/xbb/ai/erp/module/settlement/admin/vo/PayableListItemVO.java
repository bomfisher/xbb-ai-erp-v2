package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class PayableListItemVO {
    private String id;
    private String payableNo;
    private String supplierId;
    private String sourceType;
    private String payableDate;
    private String dueDate;
    private String amount;
    private String writtenOffAmount;
    private String remainingAmount;
    private String status;
    private String remark;
}
