package xbb.ai.erp.module.purchase.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/purchase/request")
@RequiredArgsConstructor
public class PurchaseRequestAdminController {

    private final PurchaseRequestAdminAppService purchaseRequestAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseRequestListItemVO>> list(@RequestBody PurchaseRequestListDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<PurchaseRequestDraftSaveVO> saveDraft(@RequestBody PurchaseRequestDraftSaveDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PurchaseRequestSubmitSaveDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PurchaseRequestDraftListItemVO>> draftList(@RequestBody PurchaseRequestDraftListDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PurchaseRequestDraftDetailVO> loadDraft(@RequestBody PurchaseRequestDraftLoadDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.loadDraft(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchaseRequestSaveDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchaseRequestDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchaseRequestAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
