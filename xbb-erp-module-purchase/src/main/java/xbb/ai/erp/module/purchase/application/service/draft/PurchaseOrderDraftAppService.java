package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface PurchaseOrderDraftAppService {
    DraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto);

    List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto);

    PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto);
}
