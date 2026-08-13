package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class ProductSpuMainDTO {
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
