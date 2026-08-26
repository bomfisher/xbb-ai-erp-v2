package xbb.ai.erp.module.settlement.admin;

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
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableSaveItemVO;
import xbb.ai.erp.module.settlement.application.service.PayableAdminAppService;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/erp/v1/settlement/payable")
@RequiredArgsConstructor
public class PayableAdminController {

    private final PayableAdminAppService payableAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PayableListItemVO>> list(@RequestBody PayableListDTO dto) {
        return ResultVO.success(payableAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PayableSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(payableAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PayableSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(payableAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PayableDraftSaveDTO dto) {
        return ResultVO.success(payableAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PayableSubmitSaveDTO dto) {
        return ResultVO.success(payableAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PayableDraftListItemVO>> draftList(@RequestBody PayableDraftListDTO dto) {
        return ResultVO.success(payableAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PayableDraftDetailVO> loadDraft(@RequestBody PayableDraftLoadDTO dto) {
        return ResultVO.success(payableAdminAppService.loadDraft(dto));
    }

    @PostMapping("/selectionFill")
    public ResultVO<SettlementSelectionFillVO> selectionFill(@RequestBody SettlementSelectionFillDTO dto) {
        return ResultVO.success(payableAdminAppService.selectionFill(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<Map<String, String>>> businessSelectQuickSearch(@RequestBody PayableListDTO dto) {
        dto.setPageNum(1);
        dto.setPageSize(5);
        return ResultVO.success(toBusinessSelectOptions(payableAdminAppService.list(dto).getList()));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<Map<String, String>>> businessSelectDialogSearch(@RequestBody PayableListDTO dto) {
        if (dto.getPageNum() == null || dto.getPageNum() < 1) {
            dto.setPageNum(1);
        }
        if (dto.getPageSize() == null || dto.getPageSize() < 1) {
            dto.setPageSize(20);
        }
        ListBaseVO<PayableListItemVO> payables = payableAdminAppService.list(dto);
        ListBaseVO<Map<String, String>> result = new ListBaseVO<>();
        result.setList(toBusinessSelectOptions(payables.getList()));
        result.setPageHelper(payables.getPageHelper());
        return ResultVO.success(result);
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<Map<String, String>> businessSelectGetById(@RequestBody PayableListDTO dto) {
        dto.setPageNum(1);
        dto.setPageSize(1);
        PayableListItemVO payable = payableAdminAppService.list(dto).getList().stream().findFirst().orElse(null);
        return ResultVO.success(payable == null ? null : toBusinessSelectOption(payable));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) { return ResultVO.success(payableAdminAppService.audit(dto)); }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) { return ResultVO.success(payableAdminAppService.unaudit(dto)); }

    @PostMapping("/void")
    public ResultVO<BaseVO> voidPayable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(payableAdminAppService.voidPayable(dto));
    }

    @PostMapping("/redFlush")
    public ResultVO<BaseVO> redFlush(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(payableAdminAppService.redFlush(dto));
    }

    private List<Map<String, String>> toBusinessSelectOptions(List<PayableListItemVO> payables) {
        return payables.stream().map(this::toBusinessSelectOption).toList();
    }

    private Map<String, String> toBusinessSelectOption(PayableListItemVO payable) {
        return Map.of(
            "id", payable.getId(),
            "code", payable.getPayableNo(),
            "name", "未核销余额 " + payable.getRemainingAmount(),
            "label", payable.getPayableNo() + "（未核销余额：" + payable.getRemainingAmount() + "）"
        );
    }
}
