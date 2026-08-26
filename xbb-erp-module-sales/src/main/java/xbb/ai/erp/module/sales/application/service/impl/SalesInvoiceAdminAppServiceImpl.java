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
import xbb.ai.erp.module.sales.application.service.SalesInvoiceAdminAppService;
import xbb.ai.erp.module.sales.application.service.draft.SalesInvoiceDraftAppService;
import xbb.ai.erp.module.sales.application.service.query.SalesInvoiceQueryAppServiceImpl;
import xbb.ai.erp.module.sales.application.service.save.SalesInvoiceSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SalesInvoiceAdminAppServiceImpl implements SalesInvoiceAdminAppService {

    private final SalesInvoiceQueryAppServiceImpl queryService;
    private final SalesInvoiceSaveAppServiceImpl saveService;
    private final SalesInvoiceDraftAppService draftService;

    @Override
    public ListBaseVO<SalesInvoiceListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<SalesInvoiceSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<SalesInvoiceSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(SalesInvoiceDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(SalesInvoiceSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<SalesInvoiceDraftListItemVO> draftList(SalesInvoiceDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public SalesInvoiceDraftDetailVO loadDraft(SalesInvoiceDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public List<SalesInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(
        SalesInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<SalesInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(
        SalesInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public SalesInvoiceBusinessSelectOptionVO businessSelectGetById(SalesInvoiceBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public Long save(SalesInvoiceSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public SalesInvoiceDetailVO detail(IdBaseDTO dto) {
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
    public List<SalesInvoiceSourceDocumentVO> sourceDocuments(SalesInvoiceSourceQueryDTO dto) {
        return queryService.sourceDocuments(dto);
    }

    @Override
    public SalesInvoiceSourcePreviewVO sourcePreview(SalesInvoiceSourceQueryDTO dto) {
        return queryService.sourcePreview(dto);
    }
}
