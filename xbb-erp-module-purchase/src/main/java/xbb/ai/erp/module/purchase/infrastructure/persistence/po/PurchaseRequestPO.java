package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_request")
public class PurchaseRequestPO extends BaseEntity {
    private String corpid;
    private Long purchaseOrgId;
    private String requestNo;
    private Long requestDeptId;
    private String applicantId;
    private String sourceType;
    private String sourceNo;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private String bizStatus;
    private String approvalStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private Integer version;
    private String remark;
    private String creatorId;
    private String modifyId;
}
