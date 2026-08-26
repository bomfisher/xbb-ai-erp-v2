package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PayableDraftSaveDTO extends PayableSaveDTO {
    private PayableDraftMetaDTO draftMeta = new PayableDraftMetaDTO();
}
