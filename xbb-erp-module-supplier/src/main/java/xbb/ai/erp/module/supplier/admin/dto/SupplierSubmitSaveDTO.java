package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierSubmitSaveDTO extends BaseDTO {
    private SupplierMainDTO main = new SupplierMainDTO();
    private SupplierSaveExtDTO ext = new SupplierSaveExtDTO();
    private SupplierSectionStateDTO sectionState = new SupplierSectionStateDTO();
    private SupplierDraftMetaDTO draftMeta = new SupplierDraftMetaDTO();
}
