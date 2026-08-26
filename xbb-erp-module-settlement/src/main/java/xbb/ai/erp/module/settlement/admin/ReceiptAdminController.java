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
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO;
import xbb.ai.erp.module.settlement.application.service.ReceiptAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/settlement/receipt")
@RequiredArgsConstructor
public class ReceiptAdminController {

    private final ReceiptAdminAppService receiptAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ReceiptListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ReceiptSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ReceiptSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody ReceiptDraftSaveDTO dto) {
        return ResultVO.success(receiptAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ReceiptSubmitSaveDTO dto) {
        return ResultVO.success(receiptAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/writeOff")
    public ResultVO<BaseVO> writeOff(@RequestBody ReceiptWriteOffDTO dto) {
        return ResultVO.success(receiptAdminAppService.writeOff(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) { return ResultVO.success(receiptAdminAppService.audit(dto)); }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) { return ResultVO.success(receiptAdminAppService.unaudit(dto)); }

    @PostMapping("/draftList")
    public ResultVO<List<ReceiptDraftListItemVO>> draftList(@RequestBody ReceiptDraftListDTO dto) {
        return ResultVO.success(receiptAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<ReceiptDraftDetailVO> loadDraft(@RequestBody ReceiptDraftLoadDTO dto) {
        return ResultVO.success(receiptAdminAppService.loadDraft(dto));
    }
}
