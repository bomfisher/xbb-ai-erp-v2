package xbb.ai.erp.module.purchase.admin;

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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/purchase/purchaseOrder")
@RequiredArgsConstructor
public class PurchaseOrderAdminController {

    private final PurchaseOrderAdminAppService purchaseOrderAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseOrderListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PurchaseOrderDraftSaveDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PurchaseOrderSubmitSaveDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.audit(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.unaudit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PurchaseOrderDraftListItemVO>> draftList(@RequestBody PurchaseOrderDraftListDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PurchaseOrderDraftDetailVO> loadDraft(@RequestBody PurchaseOrderDraftLoadDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<PurchaseOrderBusinessSelectOptionVO>> businessSelectQuickSearch(@RequestBody PurchaseOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<PurchaseOrderBusinessSelectOptionVO>> businessSelectDialogSearch(@RequestBody PurchaseOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<PurchaseOrderBusinessSelectOptionVO> businessSelectGetById(@RequestBody PurchaseOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(purchaseOrderAdminAppService.businessSelectGetById(dto));
    }
}
