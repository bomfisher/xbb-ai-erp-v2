package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.PermissionListDTO;
import xbb.ai.erp.module.org.application.service.PermissionAdminAppService;

@RestController
@RequestMapping("/erp/v1/org/permission")
@RequiredArgsConstructor
public class PermissionAdminController {

    private final PermissionAdminAppService permissionAdminAppService;

    @PostMapping("/list")
    public ResultVO<BaseVO> list(@RequestBody PermissionListDTO dto) {
        return ResultVO.success(permissionAdminAppService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<BaseVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(permissionAdminAppService.detail(dto));
    }
}
