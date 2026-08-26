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
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftSaveDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;
import xbb.ai.erp.module.settlement.application.service.PaymentAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/settlement/payment")
@RequiredArgsConstructor
public class PaymentAdminController {

    private final PaymentAdminAppService paymentAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PaymentListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(paymentAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PaymentSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(paymentAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PaymentSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(paymentAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody PaymentDraftSaveDTO dto) {
        return ResultVO.success(paymentAdminAppService.saveSupplierPaymentDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PaymentSubmitSaveDTO dto) {
        return ResultVO.success(paymentAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(paymentAdminAppService.auditSupplierPayment(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(paymentAdminAppService.unauditSupplierPayment(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<PaymentDraftListItemVO>> draftList(@RequestBody PaymentDraftListDTO dto) {
        return ResultVO.success(paymentAdminAppService.supplierPaymentDraftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<PaymentDraftDetailVO> loadDraft(@RequestBody PaymentDraftLoadDTO dto) {
        return ResultVO.success(paymentAdminAppService.loadSupplierPaymentDraft(dto));
    }
}
