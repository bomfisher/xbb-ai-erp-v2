package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSelectionFillDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundConfirmDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSelectionFillVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftDetailVO;

import java.util.List;

public interface PurchaseInboundAdminAppService {
    ListBaseVO<PurchaseInboundListItemVO> list(ListBaseDTO dto);

    SaveItemVO<PurchaseInboundSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto);

    PurchaseInboundSelectionFillVO selectionFill(PurchaseInboundSelectionFillDTO dto);

    DraftSaveVO saveDraft(PurchaseInboundDraftSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseInboundSubmitSaveDTO dto);

    BaseVO confirmInbound(PurchaseInboundConfirmDTO dto);

    List<PurchaseInboundDraftListItemVO> draftList(PurchaseInboundDraftListDTO dto);

    PurchaseInboundDraftDetailVO loadDraft(PurchaseInboundDraftLoadDTO dto);

    Long save(PurchaseInboundSaveDTO dto);

    PurchaseInboundDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
