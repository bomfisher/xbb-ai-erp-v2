package xbb.ai.erp.module.settlement.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.AdvanceReceiptBusinessSelectOptionVO;
import xbb.ai.erp.module.settlement.application.service.ReceiptAdminAppService;

@RestController
@RequestMapping("/erp/v1/settlement/advanceReceipt")
@RequiredArgsConstructor
public class AdvanceReceiptAdminController {

    private final ReceiptAdminAppService receiptAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ReceiptListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.listAdvanceReceipt(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<AdvanceReceiptBusinessSelectOptionVO>> quickSearch(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(toOptions(receiptAdminAppService.listAdvanceReceipt(dto).getList()));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<AdvanceReceiptBusinessSelectOptionVO>> dialogSearch(@RequestBody ListBaseDTO dto) {
        ListBaseVO<ReceiptListItemVO> receipts = receiptAdminAppService.listAdvanceReceipt(dto);
        ListBaseVO<AdvanceReceiptBusinessSelectOptionVO> result = new ListBaseVO<>();
        result.setList(toOptions(receipts.getList()));
        result.setPageHelper(receipts.getPageHelper());
        return ResultVO.success(result);
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<AdvanceReceiptBusinessSelectOptionVO> getById(@RequestBody IdBaseDTO dto) {
        ReceiptSaveItemVO data = receiptAdminAppService.updateAdvanceReceipt(dto).getData();
        AdvanceReceiptBusinessSelectOptionVO option = new AdvanceReceiptBusinessSelectOptionVO();
        option.setId(String.valueOf(dto.getId()));
        option.setCode(data.getMain().getReceiptNo());
        option.setName(data.getMain().getReceiptNo());
        option.setLabel(data.getMain().getReceiptNo() + "（可核销余额：" + data.getMain().getRemainingAmount() + "）");
        return ResultVO.success(option);
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ReceiptSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.addAdvanceReceipt(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ReceiptSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(receiptAdminAppService.updateAdvanceReceipt(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody ReceiptDraftSaveDTO dto) {
        dto.getMain().setReceiptType("ADVANCE_PAYMENT");
        return ResultVO.success(receiptAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ReceiptSubmitSaveDTO dto) {
        return ResultVO.success(receiptAdminAppService.saveAdvanceReceipt(dto));
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

    private List<AdvanceReceiptBusinessSelectOptionVO> toOptions(List<ReceiptListItemVO> receipts) {
        return receipts.stream().map(receipt -> {
            AdvanceReceiptBusinessSelectOptionVO option = new AdvanceReceiptBusinessSelectOptionVO();
            option.setId(receipt.getId());
            option.setCode(receipt.getReceiptNo());
            option.setName(receipt.getReceiptNo());
            option.setLabel(receipt.getReceiptNo() + "（可核销余额：" + receipt.getRemainingAmount() + "）");
            return option;
        }).toList();
    }
}
