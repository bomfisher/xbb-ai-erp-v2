package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PayableSubmitSaveDTO extends PayableSaveDTO {
    private PayableDraftMetaDTO draftMeta = new PayableDraftMetaDTO();
}
