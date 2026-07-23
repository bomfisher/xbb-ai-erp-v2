package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchasePendingTaskListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private Long purchaseOrgId;
    private String taskNo;
    private String sourceType;
    private Long sourceDocId;
    private Long sourceLineId;
    private String sourceDocNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private Integer priorityLevel;
    private String taskStatus;
    private Integer salesLinkedFlag;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
