package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
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
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderAdminAppService;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseOrderDraftAppService;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseOrderQueryAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseOrderSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PurchaseOrderAdminAppServiceImpl implements PurchaseOrderAdminAppService {

    private final PurchaseOrderQueryAppServiceImpl queryService;
    private final PurchaseOrderSaveAppServiceImpl saveService;
    private final PurchaseOrderDraftAppService draftService;

    @Override
    public ListBaseVO<PurchaseOrderListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) {
        return saveService.audit(dto);
    }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) {
        return saveService.unaudit(dto);
    }

    @Override
    public List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public List<PurchaseOrderBusinessSelectOptionVO> businessSelectQuickSearch(PurchaseOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<PurchaseOrderBusinessSelectOptionVO> businessSelectDialogSearch(PurchaseOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public PurchaseOrderBusinessSelectOptionVO businessSelectGetById(PurchaseOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public Long save(PurchaseOrderSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
