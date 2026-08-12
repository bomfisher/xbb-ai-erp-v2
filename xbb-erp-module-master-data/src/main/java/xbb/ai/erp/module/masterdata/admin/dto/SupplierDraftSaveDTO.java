package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierDraftSaveDTO extends SupplierSaveDTO {
    private SupplierDraftMetaDTO draftMeta = new SupplierDraftMetaDTO();
}
