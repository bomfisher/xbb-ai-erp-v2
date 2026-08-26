package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class PaymentWriteOffListItemVO {
    private Long id;
    private String writeoffNo;
    private Long paymentId;
    private Long payableId;
    private Long writeoffDate;
    private String amount;
    private Integer status;
    private String remark;
}
