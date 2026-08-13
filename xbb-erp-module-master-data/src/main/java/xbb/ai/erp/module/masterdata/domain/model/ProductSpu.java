package xbb.ai.erp.module.masterdata.domain.model;

import lombok.Data;

@Data
public class ProductSpu {
    private Long id;
    private String corpid;
    private String spuCode;
    private String spuName;
    private String categoryName;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
