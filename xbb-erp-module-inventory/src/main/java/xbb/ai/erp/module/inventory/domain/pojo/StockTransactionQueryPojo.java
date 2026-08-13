package xbb.ai.erp.module.inventory.domain.pojo;

import lombok.Data;

@Data
public class StockTransactionQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
}
