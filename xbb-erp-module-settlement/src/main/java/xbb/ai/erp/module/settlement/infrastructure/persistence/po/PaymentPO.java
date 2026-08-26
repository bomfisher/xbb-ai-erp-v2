package xbb.ai.erp.module.settlement.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment")
public class PaymentPO extends BaseEntity {
    private String corpid;
    private String paymentNo;
    private Long supplierId;
    private Long paymentDate;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal writtenOffAmount;
    private java.math.BigDecimal remainingAmount;
    private String paymentType;
    private String paymentMethod;
    private Long bankAccountId;
    private String bankTransactionNo;
    private Integer status;
    private Integer auditStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
}
