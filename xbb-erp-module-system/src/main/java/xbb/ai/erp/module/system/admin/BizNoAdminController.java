package xbb.ai.erp.module.system.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleQueryDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BizNoNextVO;
import xbb.ai.erp.module.system.admin.vo.BizNoRuleVO;
import xbb.ai.erp.module.system.application.service.BizNoService;

@RestController
@RequestMapping("/erp/v1/system/bizNo")
@RequiredArgsConstructor
public class BizNoAdminController {

    private final BizNoService bizNoService;

    @PostMapping("/getRule")
    public ResultVO<BizNoRuleVO> getRule(@RequestBody BizNoRuleQueryDTO dto) {
        return ResultVO.success(bizNoService.getRule(dto.getCorpid(), dto.getBusinessCode()));
    }

    @PostMapping("/saveRule")
    public ResultVO<BaseVO> saveRule(@RequestBody BizNoRuleSaveDTO dto) {
        bizNoService.saveRule(dto);
        return ResultVO.success(new BaseVO());
    }

    @PostMapping("/next")
    public ResultVO<BizNoNextVO> next(@RequestBody BizNoNextDTO dto) {
        return ResultVO.success(bizNoService.next(dto));
    }
}
