package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.DepartmentMoveDTO;
import xbb.ai.erp.module.org.admin.dto.DepartmentSaveDTO;
import xbb.ai.erp.module.org.admin.dto.DepartmentTreeDTO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.service.DepartmentAdminAppService;

@RestController
@RequestMapping("/erp/v1/org/department")
@RequiredArgsConstructor
public class DepartmentAdminController {

    private final DepartmentAdminAppService departmentAdminAppService;

    @PostMapping("/tree")
    public ResultVO<OrgAdminAssembler.DepartmentTreeResultVO> tree(@RequestBody DepartmentTreeDTO dto) {
        return ResultVO.success(departmentAdminAppService.tree(dto));
    }

    @PostMapping("/detail")
    public ResultVO<BaseVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(departmentAdminAppService.detail(dto));
    }

    @PostMapping("/save")
    public ResultVO<BaseVO> save(@RequestBody DepartmentSaveDTO dto) {
        return ResultVO.success(departmentAdminAppService.save(dto));
    }

    @PostMapping("/enable")
    public ResultVO<BaseVO> enable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(departmentAdminAppService.enable(dto));
    }

    @PostMapping("/disable")
    public ResultVO<BaseVO> disable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(departmentAdminAppService.disable(dto));
    }

    @PostMapping("/move")
    public ResultVO<BaseVO> move(@RequestBody DepartmentMoveDTO dto) {
        return ResultVO.success(departmentAdminAppService.move(dto));
    }
}
