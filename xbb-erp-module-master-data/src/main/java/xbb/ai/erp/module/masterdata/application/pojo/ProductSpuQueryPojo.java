package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;

@Data
public class ProductSpuQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String spuCode;
    private String spuName;
    private String categoryName;
    private Integer enabled;
    private String remark;
}
