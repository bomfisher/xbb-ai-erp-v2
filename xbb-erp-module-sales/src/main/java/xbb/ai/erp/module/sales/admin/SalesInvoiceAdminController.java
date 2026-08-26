package xbb.ai.erp.module.sales.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSourceQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourceDocumentVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourcePreviewVO;
import xbb.ai.erp.module.sales.application.service.SalesInvoiceAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/sales/salesInvoice")
@RequiredArgsConstructor
public class SalesInvoiceAdminController {

    private final SalesInvoiceAdminAppService salesInvoiceAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SalesInvoiceListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<SalesInvoiceSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<SalesInvoiceSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody SalesInvoiceDraftSaveDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody SalesInvoiceSubmitSaveDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.audit(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.unaudit(dto));
    }

    @PostMapping("/post")
    public ResultVO<BaseVO> post(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.post(dto));
    }

    @PostMapping("/void")
    public ResultVO<BaseVO> voidInvoice(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.voidInvoice(dto));
    }

    @PostMapping("/redFlush")
    public ResultVO<BaseVO> redFlush(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.redFlush(dto));
    }

    @PostMapping("/source/documents")
    public ResultVO<List<SalesInvoiceSourceDocumentVO>> sourceDocuments(@RequestBody SalesInvoiceSourceQueryDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.sourceDocuments(dto));
    }

    @PostMapping("/source/preview")
    public ResultVO<SalesInvoiceSourcePreviewVO> sourcePreview(@RequestBody SalesInvoiceSourceQueryDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.sourcePreview(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<SalesInvoiceDraftListItemVO>> draftList(@RequestBody SalesInvoiceDraftListDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<SalesInvoiceDraftDetailVO> loadDraft(@RequestBody SalesInvoiceDraftLoadDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<SalesInvoiceBusinessSelectOptionVO>> businessSelectQuickSearch(
        @RequestBody SalesInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<SalesInvoiceBusinessSelectOptionVO>> businessSelectDialogSearch(
        @RequestBody SalesInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<SalesInvoiceBusinessSelectOptionVO> businessSelectGetById(
        @RequestBody SalesInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesInvoiceAdminAppService.businessSelectGetById(dto));
    }
}
