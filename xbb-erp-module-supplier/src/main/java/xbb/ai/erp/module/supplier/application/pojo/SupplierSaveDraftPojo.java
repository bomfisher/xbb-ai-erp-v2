package xbb.ai.erp.module.supplier.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;

@Data
public class SupplierSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private SupplierMainDTO main = new SupplierMainDTO();
    private SupplierSaveExtPojo ext = new SupplierSaveExtPojo();
    private SupplierSectionStatePojo sectionState = new SupplierSectionStatePojo();
    private Long updatedTime;
}
