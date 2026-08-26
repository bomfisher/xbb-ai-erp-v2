package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderBusinessSelectOptionVO;

import java.util.List;

public interface PurchaseOrderAdminAppService {
    ListBaseVO<PurchaseOrderListItemVO> list(ListBaseDTO dto);

    SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto);

    PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto);

    List<PurchaseOrderBusinessSelectOptionVO> businessSelectQuickSearch(PurchaseOrderBusinessSelectQueryDTO dto);

    ListBaseVO<PurchaseOrderBusinessSelectOptionVO> businessSelectDialogSearch(PurchaseOrderBusinessSelectQueryDTO dto);

    PurchaseOrderBusinessSelectOptionVO businessSelectGetById(PurchaseOrderBusinessSelectQueryDTO dto);

    Long save(PurchaseOrderSaveDTO dto);

    PurchaseOrderDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
