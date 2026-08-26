package xbb.ai.erp.module.settlement.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("receipt_payment")
public class ReceiptPaymentPO extends BaseEntity {

    private String corpid;
    private Long receiptId;
    private Long bankAccountId;
    private String paymentMethod;
    private BigDecimal amount;
    private BigDecimal handlingFee;
    private String transactionNo;
    private String remark;
    private Integer sortNo;
    private String creatorId;
    private String modifyId;
}
