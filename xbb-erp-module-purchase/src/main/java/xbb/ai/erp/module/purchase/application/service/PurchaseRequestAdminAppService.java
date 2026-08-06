package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;

import java.util.List;

public interface PurchaseRequestAdminAppService {
    ListBaseVO<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto);

    SaveItemVO<PurchaseRequestSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseRequestSaveItemVO> updateItem(IdBaseDTO dto);

    PurchaseRequestDraftSaveVO saveDraft(PurchaseRequestDraftSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseRequestSubmitSaveDTO dto);

    List<PurchaseRequestDraftListItemVO> draftList(PurchaseRequestDraftListDTO dto);

    PurchaseRequestDraftDetailVO loadDraft(PurchaseRequestDraftLoadDTO dto);

    Long save(PurchaseRequestSaveDTO dto);

    PurchaseRequestDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
