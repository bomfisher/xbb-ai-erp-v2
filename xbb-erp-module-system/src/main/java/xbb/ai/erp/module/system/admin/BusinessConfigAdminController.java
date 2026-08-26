package xbb.ai.erp.module.system.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigCategoryQueryDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigGlobalSaveDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigCategoryVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDetailVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDocumentVO;
import xbb.ai.erp.module.system.application.service.BusinessConfigService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/system/businessConfig")
@RequiredArgsConstructor
public class BusinessConfigAdminController {

    private final BusinessConfigService businessConfigService;

    @PostMapping("/catalog")
    public ResultVO<List<BusinessConfigCategoryVO>> catalog(@RequestBody BaseDTO dto) {
        return ResultVO.success(businessConfigService.catalog());
    }

    @PostMapping("/detail")
    public ResultVO<BusinessConfigDetailVO> detail(@RequestBody BusinessConfigCategoryQueryDTO dto) {
        return ResultVO.success(businessConfigService.detail(dto));
    }

    @PostMapping("/save")
    public ResultVO<BusinessConfigDocumentVO> save(@RequestBody BusinessConfigSaveDTO dto) {
        return ResultVO.success(businessConfigService.save(dto));
    }

    @PostMapping("/saveGlobal")
    public ResultVO<BusinessConfigDetailVO> saveGlobal(@RequestBody BusinessConfigGlobalSaveDTO dto) {
        return ResultVO.success(businessConfigService.saveGlobal(dto));
    }
}
