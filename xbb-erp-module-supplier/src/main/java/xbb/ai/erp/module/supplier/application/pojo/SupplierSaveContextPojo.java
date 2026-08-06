package xbb.ai.erp.module.supplier.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;

@Data
public class SupplierSaveContextPojo {
    private String corpid;
    private SupplierMainDTO main = new SupplierMainDTO();
    private SupplierSaveExtPojo ext = new SupplierSaveExtPojo();
    private SupplierSectionStatePojo sectionState = new SupplierSectionStatePojo();
    private SupplierDraftMetaPojo draftMeta = new SupplierDraftMetaPojo();
    private Integer submitMode;
}
