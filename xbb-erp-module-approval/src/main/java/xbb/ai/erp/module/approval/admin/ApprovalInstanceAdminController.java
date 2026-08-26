package xbb.ai.erp.module.approval.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceListDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceActionDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceDetailVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceListItemVO;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceService;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceCommandService;
import xbb.ai.erp.module.approval.contract.ApprovalPlatformApi;

import java.util.List;

/**
 * 审批中心实例查询接口。
 */
@RestController
@RequestMapping("/erp/v1/approval/instance")
@RequiredArgsConstructor
public class ApprovalInstanceAdminController {

    private final ApprovalInstanceService approvalInstanceService;
    private final ApprovalInstanceCommandService approvalInstanceCommandService;
    private final ApprovalPlatformApi approvalPlatformApi;

    @PostMapping("/list")
    public ResultVO<List<ApprovalInstanceListItemVO>> list(@RequestBody ApprovalInstanceListDTO dto) {
        return ResultVO.success(approvalInstanceService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ApprovalInstanceDetailVO> detail(@RequestBody ApprovalInstanceDetailDTO dto) {
        return ResultVO.success(approvalInstanceService.detail(dto));
    }

    @PostMapping("/approve")
    public ResultVO<BaseVO> approve(@RequestBody ApprovalInstanceActionDTO dto) {
        approvalInstanceCommandService.approve(dto);
        return ResultVO.success(new BaseVO());
    }

    @PostMapping("/reject")
    public ResultVO<BaseVO> reject(@RequestBody ApprovalInstanceActionDTO dto) {
        approvalInstanceCommandService.reject(dto);
        return ResultVO.success(new BaseVO());
    }

    @PostMapping("/withdraw")
    public ResultVO<BaseVO> withdraw(@RequestBody ApprovalInstanceActionDTO dto) {
        approvalPlatformApi.withdraw(dto.getCorpid(), dto.getInstanceId(), dto.getUserId(), dto.getComment());
        return ResultVO.success(new BaseVO());
    }
}
