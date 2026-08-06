package xbb.ai.erp.module.product.domain.pojo;

import lombok.Data;

@Data
public class ProductQueryPojo {

    private String corpid;
    private String keyword;
    private Integer page;
    private Integer pageSize;
    private Integer offset;
}
