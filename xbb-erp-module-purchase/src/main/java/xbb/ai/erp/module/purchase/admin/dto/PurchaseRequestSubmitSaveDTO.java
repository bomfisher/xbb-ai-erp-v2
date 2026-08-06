package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseRequestSubmitSaveDTO extends BaseDTO {
    private PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    private List<PurchaseRequestItemMainDTO> items = new ArrayList<>();
    private PurchaseRequestSectionStateDTO sectionState = new PurchaseRequestSectionStateDTO();
    private PurchaseRequestDraftMetaDTO draftMeta = new PurchaseRequestDraftMetaDTO();
}
