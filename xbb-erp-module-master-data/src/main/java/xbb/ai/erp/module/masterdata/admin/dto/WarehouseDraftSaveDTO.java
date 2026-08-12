package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseDraftSaveDTO extends WarehouseSaveDTO {
    private WarehouseDraftMetaDTO draftMeta = new WarehouseDraftMetaDTO();
}
