package xbb.ai.erp.module.settlement.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentWriteOffSaveDTO;
import xbb.ai.erp.module.settlement.application.service.PaymentWriteOffService;
import xbb.ai.erp.module.settlement.admin.vo.PaymentWriteOffListItemVO;

import java.util.List;
import xbb.ai.erp.module.settlement.admin.vo.PaymentWriteOffSourceVO;

@RestController
@RequestMapping("/erp/v1/settlement/paymentWriteoff")
@RequiredArgsConstructor
public class PaymentWriteOffAdminController {
    private final PaymentWriteOffService service;

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody PaymentWriteOffSaveDTO dto) {
        return ResultVO.success(service.writeOff(dto));
    }

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PaymentWriteOffListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(service.list(dto));
    }

    @PostMapping("/reverse")
    public ResultVO<BaseVO> reverse(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(service.reverse(dto));
    }

    @PostMapping("/advanceSources")
    public ResultVO<List<PaymentWriteOffSourceVO>> advanceSources(@RequestBody PaymentWriteOffSaveDTO dto) {
        return ResultVO.success(service.advanceSources(dto.getCorpid(), dto.getSupplierId()));
    }

    @PostMapping("/payableSources")
    public ResultVO<List<PaymentWriteOffSourceVO>> payableSources(@RequestBody PaymentWriteOffSaveDTO dto) {
        return ResultVO.success(service.payableSources(dto.getCorpid(), dto.getSupplierId()));
    }
}
