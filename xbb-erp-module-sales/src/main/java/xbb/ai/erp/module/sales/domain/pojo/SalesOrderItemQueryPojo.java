package xbb.ai.erp.module.sales.domain.pojo;

import lombok.Data;

@Data
public class SalesOrderItemQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long salesOrderId;
    private Long skuId;
}
