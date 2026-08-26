package xbb.ai.erp.module.settlement.admin;

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
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableBusinessSelectQueryDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableBusinessSelectOptionVO;
import xbb.ai.erp.module.settlement.application.service.ReceivableAdminAppService;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/settlement/receivable")
@RequiredArgsConstructor
public class ReceivableAdminController {

    private final ReceivableAdminAppService receivableAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ReceivableListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(receivableAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ReceivableSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(receivableAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ReceivableSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(receivableAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody ReceivableDraftSaveDTO dto) {
        return ResultVO.success(receivableAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ReceivableSubmitSaveDTO dto) {
        return ResultVO.success(receivableAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<ReceivableDraftListItemVO>> draftList(@RequestBody ReceivableDraftListDTO dto) {
        return ResultVO.success(receivableAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<ReceivableDraftDetailVO> loadDraft(@RequestBody ReceivableDraftLoadDTO dto) {
        return ResultVO.success(receivableAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<ReceivableBusinessSelectOptionVO>> businessSelectQuickSearch(
            @RequestBody ReceivableBusinessSelectQueryDTO dto) {
        return ResultVO.success(receivableAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<ReceivableBusinessSelectOptionVO>> businessSelectDialogSearch(
            @RequestBody ReceivableBusinessSelectQueryDTO dto) {
        return ResultVO.success(receivableAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<ReceivableBusinessSelectOptionVO> businessSelectGetById(
            @RequestBody ReceivableBusinessSelectQueryDTO dto) {
        return ResultVO.success(receivableAdminAppService.businessSelectGetById(dto));
    }

    @PostMapping("/selectionFill")
    public ResultVO<SettlementSelectionFillVO> selectionFill(@RequestBody SettlementSelectionFillDTO dto) {
        return ResultVO.success(receivableAdminAppService.selectionFill(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) { return ResultVO.success(receivableAdminAppService.audit(dto)); }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) { return ResultVO.success(receivableAdminAppService.unaudit(dto)); }

    @PostMapping("/void")
    public ResultVO<BaseVO> voidReceivable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(receivableAdminAppService.voidReceivable(dto));
    }

    @PostMapping("/redFlush")
    public ResultVO<BaseVO> redFlush(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(receivableAdminAppService.redFlush(dto));
    }
}
