package xbb.ai.erp.module.purchase.domain.pojo;

import lombok.Data;

@Data
public class PurchaseInboundItemQueryPojo {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String corpid;
    private Long inboundId;
    private Long sourceLineId;
    private Long skuId;
    private Long warehouseId;
    private String batchNo;
    private String serialNo;
}
