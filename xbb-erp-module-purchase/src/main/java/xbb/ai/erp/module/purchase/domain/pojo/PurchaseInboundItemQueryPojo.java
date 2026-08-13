package xbb.ai.erp.module.purchase.domain.pojo;

import lombok.Data;

@Data
public class PurchaseInboundItemQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long purchaseInboundId;
    private Long purchaseOrderItemId;
    private Long skuId;
    private String skuName;
    private String unitName;
}
