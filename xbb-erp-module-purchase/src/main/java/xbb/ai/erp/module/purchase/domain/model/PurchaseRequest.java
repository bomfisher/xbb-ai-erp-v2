package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long id;
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
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
