package xbb.ai.erp.module.approval.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowCatalogDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowIdDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowListDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowSaveDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowCatalogVO;
import xbb.ai.erp.module.approval.application.service.ApprovalFlowService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/approval/flow")
@RequiredArgsConstructor
public class ApprovalFlowAdminController {

    private final ApprovalFlowService approvalFlowService;

    @PostMapping("/catalog")
    public ResultVO<List<ApprovalFlowCatalogVO>> catalog(@RequestBody ApprovalFlowCatalogDTO dto) {
        return ResultVO.success(approvalFlowService.catalog(dto));
    }

    @PostMapping("/list")
    public ResultVO<List<ApprovalFlowVO>> list(@RequestBody ApprovalFlowListDTO dto) {
        return ResultVO.success(approvalFlowService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ApprovalFlowVO> detail(@RequestBody ApprovalFlowDetailDTO dto) {
        return ResultVO.success(approvalFlowService.detail(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<ApprovalFlowVO> saveDraft(@RequestBody ApprovalFlowSaveDTO dto) {
        return ResultVO.success(approvalFlowService.saveDraft(dto));
    }

    @PostMapping("/publish")
    public ResultVO<ApprovalFlowVO> publish(@RequestBody ApprovalFlowIdDTO dto) {
        return ResultVO.success(approvalFlowService.publish(dto));
    }

    @PostMapping("/createNextVersion")
    public ResultVO<ApprovalFlowVO> createNextVersion(@RequestBody ApprovalFlowIdDTO dto) {
        return ResultVO.success(approvalFlowService.createNextVersion(dto));
    }

    @PostMapping("/disable")
    public ResultVO<ApprovalFlowVO> disable(@RequestBody ApprovalFlowIdDTO dto) {
        return ResultVO.success(approvalFlowService.disable(dto));
    }

    @PostMapping("/delete")
    public ResultVO<BaseVO> delete(@RequestBody ApprovalFlowIdDTO dto) {
        approvalFlowService.remove(dto);
        return ResultVO.success(new BaseVO());
    }
}
