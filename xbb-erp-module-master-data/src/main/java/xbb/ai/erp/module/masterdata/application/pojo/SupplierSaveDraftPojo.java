package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierMainDTO;

@Data
public class SupplierSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private SupplierMainDTO main;
    private Long updatedTime;
}
