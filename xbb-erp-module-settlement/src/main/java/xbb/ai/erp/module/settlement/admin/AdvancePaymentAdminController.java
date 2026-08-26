package xbb.ai.erp.module.settlement.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;
import xbb.ai.erp.module.settlement.application.service.PaymentAdminAppService;

@RestController
@RequestMapping("/erp/v1/settlement/advancePayment")
@RequiredArgsConstructor
public class AdvancePaymentAdminController {
    private final PaymentAdminAppService service;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PaymentListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(service.listAdvancePayment(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PaymentSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(service.addAdvancePayment(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PaymentSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(service.updateAdvancePayment(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PaymentSubmitSaveDTO dto) {
        return ResultVO.success(service.saveAdvancePayment(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(service.auditAdvancePayment(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(service.unauditAdvancePayment(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PaymentDraftSaveDTO dto) {
        return ResultVO.success(service.saveAdvancePaymentDraft(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<java.util.List<PaymentDraftListItemVO>> draftList(@RequestBody PaymentDraftListDTO dto) {
        return ResultVO.success(service.advancePaymentDraftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PaymentDraftDetailVO> loadDraft(@RequestBody PaymentDraftLoadDTO dto) {
        return ResultVO.success(service.loadAdvancePaymentDraft(dto));
    }

}
