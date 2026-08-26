package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceivableDraftSaveDTO extends ReceivableSaveDTO {
    private ReceivableDraftMetaDTO draftMeta = new ReceivableDraftMetaDTO();
}
