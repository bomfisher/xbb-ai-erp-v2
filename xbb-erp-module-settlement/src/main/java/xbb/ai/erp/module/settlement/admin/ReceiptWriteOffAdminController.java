package xbb.ai.erp.module.settlement.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSourceQueryDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSourceVO;
import java.util.List;
import xbb.ai.erp.module.settlement.application.service.ReceiptWriteOffAdminAppService;

@RestController
@RequestMapping("/erp/v1/settlement/receiptWriteoff")
@RequiredArgsConstructor
public class ReceiptWriteOffAdminController {
    private final ReceiptWriteOffAdminAppService service;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ReceiptWriteOffListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(service.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ReceiptWriteOffSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(service.addItem(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ReceiptWriteOffSaveDTO dto) {
        return ResultVO.success(service.saveAndSubmit(dto));
    }

    @PostMapping("/advanceSources")
    public ResultVO<List<ReceiptWriteOffSourceVO>> advanceSources(@RequestBody ReceiptWriteOffSourceQueryDTO dto) {
        return ResultVO.success(service.findAdvanceSources(dto));
    }

    @PostMapping("/receivableSources")
    public ResultVO<List<ReceiptWriteOffSourceVO>> receivableSources(@RequestBody ReceiptWriteOffSourceQueryDTO dto) {
        return ResultVO.success(service.findReceivableSources(dto));
    }

    @PostMapping("/reverse")
    public ResultVO<BaseVO> reverse(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(service.reverse(dto));
    }
}
