package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
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
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftDetailVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseInvoiceAdminAppService;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseInvoiceDraftAppService;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseInvoiceQueryAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseInvoiceSaveAppServiceImpl;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourceDocumentVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourcePreviewVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceBusinessSelectOptionVO;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PurchaseInvoiceAdminAppServiceImpl implements PurchaseInvoiceAdminAppService {

    private final PurchaseInvoiceQueryAppServiceImpl queryService;
    private final PurchaseInvoiceSaveAppServiceImpl saveService;
    private final PurchaseInvoiceDraftAppService draftService;

    @Override
    public ListBaseVO<PurchaseInvoiceListItemVO> list(PurchaseInvoiceListDTO dto) {
        return queryService.list(dto);
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
    public BaseVO post(IdBaseDTO dto) {
        return saveService.post(dto);
    }

    @Override
    public BaseVO voidInvoice(IdBaseDTO dto) {
        return saveService.voidInvoice(dto);
    }

    @Override
    public BaseVO redFlush(IdBaseDTO dto) {
        return saveService.redFlush(dto);
    }

    @Override
    public SaveItemVO<PurchaseInvoiceSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<PurchaseInvoiceSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(PurchaseInvoiceDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(PurchaseInvoiceSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<PurchaseInvoiceDraftListItemVO> draftList(PurchaseInvoiceDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public PurchaseInvoiceDraftDetailVO loadDraft(PurchaseInvoiceDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(PurchaseInvoiceSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public PurchaseInvoiceDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public List<PurchaseInvoiceSourceDocumentVO> sourceDocuments(PurchaseInvoiceSourceQueryDTO dto) {
        return queryService.sourceDocuments(dto);
    }

    @Override
    public PurchaseInvoiceSourcePreviewVO sourcePreview(PurchaseInvoiceSourceQueryDTO dto) {
        return queryService.sourcePreview(dto);
    }

    @Override
    public List<PurchaseInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(
        PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(
        PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public PurchaseInvoiceBusinessSelectOptionVO businessSelectGetById(PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }
}
