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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSelectionFillDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundConfirmDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSelectionFillVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseInboundAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/purchase/purchaseInbound")
@RequiredArgsConstructor
public class PurchaseInboundAdminController {

    private final PurchaseInboundAdminAppService purchaseInboundAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseInboundListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseInboundSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseInboundSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.updateItem(dto));
    }

    @PostMapping("/selectionFill")
    public ResultVO<PurchaseInboundSelectionFillVO> selectionFill(@RequestBody PurchaseInboundSelectionFillDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.selectionFill(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PurchaseInboundDraftSaveDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PurchaseInboundSubmitSaveDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/confirmInbound")
    public ResultVO<BaseVO> confirmInbound(@RequestBody PurchaseInboundConfirmDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.confirmInbound(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PurchaseInboundDraftListItemVO>> draftList(@RequestBody PurchaseInboundDraftListDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PurchaseInboundDraftDetailVO> loadDraft(@RequestBody PurchaseInboundDraftLoadDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.loadDraft(dto));
    }
}
