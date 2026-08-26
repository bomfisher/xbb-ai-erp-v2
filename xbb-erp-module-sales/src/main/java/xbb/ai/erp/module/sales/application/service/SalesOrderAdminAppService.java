package xbb.ai.erp.module.sales.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
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

import java.util.List;

public interface SalesOrderAdminAppService {
    ListBaseVO<SalesOrderListItemVO> list(ListBaseDTO dto);

    SaveItemVO<SalesOrderSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SalesOrderSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(SalesOrderDraftSaveDTO dto);

    BaseVO saveAndSubmit(SalesOrderSubmitSaveDTO dto);

    List<SalesOrderDraftListItemVO> draftList(SalesOrderDraftListDTO dto);

    SalesOrderDraftDetailVO loadDraft(SalesOrderDraftLoadDTO dto);

    List<SalesOrderBusinessSelectOptionVO> businessSelectQuickSearch(SalesOrderBusinessSelectQueryDTO dto);

    ListBaseVO<SalesOrderBusinessSelectOptionVO> businessSelectDialogSearch(SalesOrderBusinessSelectQueryDTO dto);

    SalesOrderBusinessSelectOptionVO businessSelectGetById(SalesOrderBusinessSelectQueryDTO dto);

    SalesOrderItemStockVO queryItemStock(SalesOrderItemStockQueryDTO dto);

    Long save(SalesOrderSaveDTO dto);

    SalesOrderDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);
}
