package xbb.ai.erp.module.purchase.domain.pojo;

import lombok.Data;

@Data
public class PurchaseOrderItemQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long purchaseOrderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String unitName;
}
