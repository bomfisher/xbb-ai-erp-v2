package xbb.ai.erp.module.sales.application.service.impl;

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
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemStockQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderItemStockVO;
import xbb.ai.erp.module.sales.application.service.SalesOrderAdminAppService;
import xbb.ai.erp.module.sales.application.service.draft.SalesOrderDraftAppService;
import xbb.ai.erp.module.sales.application.service.query.SalesOrderQueryAppServiceImpl;
import xbb.ai.erp.module.sales.application.service.save.SalesOrderSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SalesOrderAdminAppServiceImpl implements SalesOrderAdminAppService {

    private final SalesOrderQueryAppServiceImpl queryService;
    private final SalesOrderSaveAppServiceImpl saveService;
    private final SalesOrderDraftAppService draftService;

    @Override
    public ListBaseVO<SalesOrderListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<SalesOrderSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<SalesOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(SalesOrderDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(SalesOrderSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<SalesOrderDraftListItemVO> draftList(SalesOrderDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public SalesOrderDraftDetailVO loadDraft(SalesOrderDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public List<SalesOrderBusinessSelectOptionVO> businessSelectQuickSearch(SalesOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<SalesOrderBusinessSelectOptionVO> businessSelectDialogSearch(SalesOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public SalesOrderBusinessSelectOptionVO businessSelectGetById(SalesOrderBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public SalesOrderItemStockVO queryItemStock(SalesOrderItemStockQueryDTO dto) {
        return queryService.queryItemStock(dto);
    }

    @Override
    public Long save(SalesOrderSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public SalesOrderDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) {
        return saveService.audit(dto);
    }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) {
        return saveService.unaudit(dto);
    }
}
