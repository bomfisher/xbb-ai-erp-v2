package xbb.ai.erp.module.settlement.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_writeoff")
public class PaymentWriteOffPO extends BaseEntity {
    private String corpid;
    private Long supplierId;
    private String writeoffNo;
    private Long paymentId;
    private Long payableId;
    private Long writeoffDate;
    private BigDecimal amount;
    private Integer status;
    private Long reversedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
