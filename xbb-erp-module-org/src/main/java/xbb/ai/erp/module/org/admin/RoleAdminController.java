package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.RoleListDTO;
import xbb.ai.erp.module.org.admin.dto.RolePermissionSaveDTO;
import xbb.ai.erp.module.org.admin.dto.RoleSaveDTO;
import xbb.ai.erp.module.org.admin.vo.RoleListItemVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.service.RoleAdminAppService;

@RestController
@RequestMapping("/erp/v1/org/role")
@RequiredArgsConstructor
public class RoleAdminController {

    private final RoleAdminAppService roleAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<RoleListItemVO>> list(@RequestBody RoleListDTO dto) {
        return ResultVO.success(roleAdminAppService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<BaseVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(roleAdminAppService.detail(dto));
    }

    @PostMapping("/save")
    public ResultVO<BaseVO> save(@RequestBody RoleSaveDTO dto) {
        return ResultVO.success(roleAdminAppService.save(dto));
    }

    @PostMapping("/enable")
    public ResultVO<BaseVO> enable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(roleAdminAppService.enable(dto));
    }

    @PostMapping("/disable")
    public ResultVO<BaseVO> disable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(roleAdminAppService.disable(dto));
    }

    @PostMapping("/permissionDetail")
    public ResultVO<OrgAdminAssembler.RolePermissionDetailResultVO> permissionDetail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(roleAdminAppService.permissionDetail(dto));
    }

    @PostMapping("/savePermission")
    public ResultVO<BaseVO> savePermission(@RequestBody RolePermissionSaveDTO dto) {
        return ResultVO.success(roleAdminAppService.savePermission(dto));
    }
}
