package xbb.ai.erp.module.supplier.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;

@Data
public class SupplierDraftDetailVO {
    private SupplierMainDTO main = new SupplierMainDTO();
    private SupplierSaveExtVO ext = new SupplierSaveExtVO();
    private SupplierSectionStateVO sectionState = new SupplierSectionStateVO();
    private SupplierDraftMetaVO draftMeta = new SupplierDraftMetaVO();
}
