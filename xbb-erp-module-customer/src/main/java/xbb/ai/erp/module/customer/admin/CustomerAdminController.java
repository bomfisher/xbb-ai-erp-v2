package xbb.ai.erp.module.customer.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;

@RestController
@RequestMapping("/erp/v1/customer")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerAdminAppService customerAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<CustomerListItemVO>> list(@RequestBody CustomerListDTO dto) {
        return ResultVO.success(customerAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(customerAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(customerAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody CustomerSaveDTO dto) {
        return ResultVO.success(customerAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<CustomerDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(customerAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        customerAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
