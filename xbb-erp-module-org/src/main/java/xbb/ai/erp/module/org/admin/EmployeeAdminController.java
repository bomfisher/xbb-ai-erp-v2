package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.EmployeeBatchDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeIdDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeListDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeSaveDTO;
import xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO;
import xbb.ai.erp.module.org.application.service.EmployeeAdminAppService;

@RestController
@RequestMapping("/erp/v1/org/employee")
@RequiredArgsConstructor
public class EmployeeAdminController {

    private final EmployeeAdminAppService employeeAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<EmployeeListItemVO>> list(@RequestBody EmployeeListDTO dto) {
        return ResultVO.success(employeeAdminAppService.list(dto));
    }

    @PostMapping("/save")
    public ResultVO<BaseVO> save(@RequestBody EmployeeSaveDTO dto) {
        return ResultVO.success(employeeAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<BaseVO> detail(@RequestBody EmployeeIdDTO dto) {
        return ResultVO.success(employeeAdminAppService.detail(dto));
    }

    @PostMapping("/enable")
    public ResultVO<BaseVO> enable(@RequestBody EmployeeIdDTO dto) {
        return ResultVO.success(employeeAdminAppService.enable(dto));
    }

    @PostMapping("/disable")
    public ResultVO<BaseVO> disable(@RequestBody EmployeeIdDTO dto) {
        return ResultVO.success(employeeAdminAppService.disable(dto));
    }

    @PostMapping("/resign")
    public ResultVO<BaseVO> resign(@RequestBody EmployeeIdDTO dto) {
        return ResultVO.success(employeeAdminAppService.resign(dto));
    }

    @PostMapping("/delete")
    public ResultVO<BaseVO> delete(@RequestBody EmployeeBatchDTO dto) {
        return ResultVO.success(employeeAdminAppService.delete(dto));
    }
}
