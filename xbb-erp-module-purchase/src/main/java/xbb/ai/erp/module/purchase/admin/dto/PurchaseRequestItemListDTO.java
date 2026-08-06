package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseRequestItemListDTO extends ListBaseDTO {
    private Long id;
    private String corpid;
    private Long requestId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private Long purchaseUnitId;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
