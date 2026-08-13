package xbb.ai.erp.module.masterdata.domain.pojo;

import lombok.Data;

@Data
public class ProductSkuQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String unitName;
    private Integer enabled;
}
