package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuMainDTO;

@Data
public class ProductSpuSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private ProductSpuMainDTO main;
    private Long updatedTime;
}
