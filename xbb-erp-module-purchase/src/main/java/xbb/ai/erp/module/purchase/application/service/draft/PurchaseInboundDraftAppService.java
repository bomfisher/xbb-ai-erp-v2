package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface PurchaseInboundDraftAppService {
    DraftSaveVO saveDraft(PurchaseInboundDraftSaveDTO dto);

    List<PurchaseInboundDraftListItemVO> draftList(PurchaseInboundDraftListDTO dto);

    PurchaseInboundDraftDetailVO loadDraft(PurchaseInboundDraftLoadDTO dto);
}
