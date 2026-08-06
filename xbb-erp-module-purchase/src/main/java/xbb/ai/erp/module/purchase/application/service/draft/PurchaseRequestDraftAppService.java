package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;

import java.util.List;

public interface PurchaseRequestDraftAppService {
    PurchaseRequestDraftSaveVO saveDraft(PurchaseRequestDraftSaveDTO dto);

    List<PurchaseRequestDraftListItemVO> draftList(PurchaseRequestDraftListDTO dto);

    PurchaseRequestDraftDetailVO loadDraft(PurchaseRequestDraftLoadDTO dto);
}
