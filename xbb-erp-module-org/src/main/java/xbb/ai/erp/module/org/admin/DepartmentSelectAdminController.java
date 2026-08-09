package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.DepartmentSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.DepartmentSelectOptionVO;
import xbb.ai.erp.module.org.application.service.DepartmentSelectAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/org/departmentSelect")
@RequiredArgsConstructor
public class DepartmentSelectAdminController {

    private final DepartmentSelectAppService departmentSelectAppService;

    @PostMapping("/quickSearch")
    public ResultVO<List<DepartmentSelectOptionVO>> quickSearch(@RequestBody DepartmentSelectQueryDTO dto) {
        return ResultVO.success(departmentSelectAppService.quickSearch(dto));
    }

    @PostMapping("/dialogSearch")
    public ResultVO<ListBaseVO<DepartmentSelectOptionVO>> dialogSearch(@RequestBody DepartmentSelectQueryDTO dto) {
        return ResultVO.success(departmentSelectAppService.dialogSearch(dto));
    }

    @PostMapping("/getById")
    public ResultVO<DepartmentSelectOptionVO> getById(@RequestBody DepartmentSelectQueryDTO dto) {
        return ResultVO.success(departmentSelectAppService.getById(dto));
    }
}
