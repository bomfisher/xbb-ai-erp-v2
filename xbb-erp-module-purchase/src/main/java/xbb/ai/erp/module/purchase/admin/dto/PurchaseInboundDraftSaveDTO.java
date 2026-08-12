package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInboundDraftSaveDTO extends PurchaseInboundSaveDTO {
    private PurchaseInboundDraftMetaDTO draftMeta = new PurchaseInboundDraftMetaDTO();
}
