package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;

@Data
public class ProductSpuListItemVO {
    private Long id;
    private String spuCode;
    private String spuName;
    private String categoryName;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
