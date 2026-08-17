package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOutboundDraftSaveDTO extends SalesOutboundSaveDTO {
    private SalesOutboundDraftMetaDTO draftMeta = new SalesOutboundDraftMetaDTO();
}
