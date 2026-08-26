package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptSubmitSaveDTO extends ReceiptSaveDTO {
    private ReceiptDraftMetaDTO draftMeta = new ReceiptDraftMetaDTO();
}
