package xbb.ai.erp.module.purchase.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourceDocumentVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourcePreviewVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseInvoiceAdminAppService;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceBusinessSelectOptionVO;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/purchase/purchaseInvoice")
@RequiredArgsConstructor
public class PurchaseInvoiceAdminController {

    private final PurchaseInvoiceAdminAppService purchaseInvoiceAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseInvoiceListItemVO>> list(@RequestBody PurchaseInvoiceListDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseInvoiceSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseInvoiceSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PurchaseInvoiceDraftSaveDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PurchaseInvoiceSubmitSaveDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PurchaseInvoiceDraftListItemVO>> draftList(@RequestBody PurchaseInvoiceDraftListDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.draftList(dto));
    }

    @PostMapping("/source/documents")
    public ResultVO<List<PurchaseInvoiceSourceDocumentVO>> sourceDocuments(
        @RequestBody PurchaseInvoiceSourceQueryDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.sourceDocuments(dto));
    }

    @PostMapping("/source/preview")
    public ResultVO<PurchaseInvoiceSourcePreviewVO> sourcePreview(
        @RequestBody PurchaseInvoiceSourceQueryDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.sourcePreview(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PurchaseInvoiceDraftDetailVO> loadDraft(@RequestBody PurchaseInvoiceDraftLoadDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<PurchaseInvoiceBusinessSelectOptionVO>> businessSelectQuickSearch(
        @RequestBody PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO>> businessSelectDialogSearch(
        @RequestBody PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<PurchaseInvoiceBusinessSelectOptionVO> businessSelectGetById(
        @RequestBody PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.businessSelectGetById(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.audit(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.unaudit(dto));
    }

    @PostMapping("/post")
    public ResultVO<BaseVO> post(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.post(dto));
    }

    @PostMapping("/void")
    public ResultVO<BaseVO> voidInvoice(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.voidInvoice(dto));
    }

    @PostMapping("/redFlush")
    public ResultVO<BaseVO> redFlush(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInvoiceAdminAppService.redFlush(dto));
    }
}
