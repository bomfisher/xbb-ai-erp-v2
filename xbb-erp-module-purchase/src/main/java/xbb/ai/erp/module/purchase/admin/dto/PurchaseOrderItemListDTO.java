package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderItemListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private Long orderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private Long purchaseUnitId;
    private Long warehouseId;
    private Integer isGift;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
