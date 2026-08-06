package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseRequestListDTO extends ListBaseDTO {
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
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
