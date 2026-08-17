package xbb.ai.erp.module.sales.domain.pojo;

import lombok.Data;

@Data
public class SalesOutboundItemQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long salesOutboundId;
    private Long salesOrderItemId;
    private Long skuId;
}
