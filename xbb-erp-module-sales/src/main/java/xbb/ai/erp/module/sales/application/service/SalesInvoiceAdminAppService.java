package xbb.ai.erp.module.sales.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSourceQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourceDocumentVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourcePreviewVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftDetailVO;

import java.util.List;

public interface SalesInvoiceAdminAppService {
    ListBaseVO<SalesInvoiceListItemVO> list(ListBaseDTO dto);

    SaveItemVO<SalesInvoiceSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SalesInvoiceSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(SalesInvoiceDraftSaveDTO dto);

    BaseVO saveAndSubmit(SalesInvoiceSubmitSaveDTO dto);

    List<SalesInvoiceDraftListItemVO> draftList(SalesInvoiceDraftListDTO dto);

    SalesInvoiceDraftDetailVO loadDraft(SalesInvoiceDraftLoadDTO dto);

    List<SalesInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(SalesInvoiceBusinessSelectQueryDTO dto);

    ListBaseVO<SalesInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(SalesInvoiceBusinessSelectQueryDTO dto);

    SalesInvoiceBusinessSelectOptionVO businessSelectGetById(SalesInvoiceBusinessSelectQueryDTO dto);

    Long save(SalesInvoiceSaveDTO dto);

    SalesInvoiceDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    BaseVO post(IdBaseDTO dto);

    BaseVO voidInvoice(IdBaseDTO dto);

    BaseVO redFlush(IdBaseDTO dto);

    List<SalesInvoiceSourceDocumentVO> sourceDocuments(SalesInvoiceSourceQueryDTO dto);

    SalesInvoiceSourcePreviewVO sourcePreview(SalesInvoiceSourceQueryDTO dto);
}
