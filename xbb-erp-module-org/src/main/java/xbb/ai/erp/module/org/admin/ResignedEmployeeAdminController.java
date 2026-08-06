package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.EmployeeIdDTO;
import xbb.ai.erp.module.org.admin.dto.ResignedEmployeeListDTO;
import xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO;
import xbb.ai.erp.module.org.application.service.EmployeeAdminAppService;

@RestController
@RequestMapping("/erp/v1/org/resigned-employee")
@RequiredArgsConstructor
public class ResignedEmployeeAdminController {

    private final EmployeeAdminAppService employeeAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<EmployeeListItemVO>> list(@RequestBody ResignedEmployeeListDTO dto) {
        return ResultVO.success(employeeAdminAppService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<BaseVO> detail(@RequestBody EmployeeIdDTO dto) {
        return ResultVO.success(employeeAdminAppService.detail(dto));
    }
}
