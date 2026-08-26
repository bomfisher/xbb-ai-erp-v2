package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDraftSaveDTO extends PaymentSaveDTO {
    private PaymentDraftMetaDTO draftMeta = new PaymentDraftMetaDTO();
}
