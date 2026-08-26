package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourceDocumentVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourcePreviewVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceBusinessSelectOptionVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftDetailVO;

import java.util.List;

public interface PurchaseInvoiceAdminAppService {
    ListBaseVO<PurchaseInvoiceListItemVO> list(PurchaseInvoiceListDTO dto);

    SaveItemVO<PurchaseInvoiceSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseInvoiceSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(PurchaseInvoiceDraftSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseInvoiceSubmitSaveDTO dto);

    List<PurchaseInvoiceDraftListItemVO> draftList(PurchaseInvoiceDraftListDTO dto);

    PurchaseInvoiceDraftDetailVO loadDraft(PurchaseInvoiceDraftLoadDTO dto);

    Long save(PurchaseInvoiceSaveDTO dto);

    PurchaseInvoiceDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    List<PurchaseInvoiceSourceDocumentVO> sourceDocuments(PurchaseInvoiceSourceQueryDTO dto);

    PurchaseInvoiceSourcePreviewVO sourcePreview(PurchaseInvoiceSourceQueryDTO dto);

    List<PurchaseInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(PurchaseInvoiceBusinessSelectQueryDTO dto);

    ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(PurchaseInvoiceBusinessSelectQueryDTO dto);

    PurchaseInvoiceBusinessSelectOptionVO businessSelectGetById(PurchaseInvoiceBusinessSelectQueryDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    BaseVO post(IdBaseDTO dto);

    BaseVO voidInvoice(IdBaseDTO dto);

    BaseVO redFlush(IdBaseDTO dto);
}
