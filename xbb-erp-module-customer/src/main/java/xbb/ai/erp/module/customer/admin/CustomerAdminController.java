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
    public ListBaseVO<CustomerListItemVO> list(@RequestBody CustomerListDTO dto) {
        return customerAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<CustomerSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return customerAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<CustomerSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return customerAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody CustomerSaveDTO dto) {
        return customerAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public CustomerDetailVO detail(@RequestBody IdBaseDTO dto) {
        return customerAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        customerAdminAppService.delete(dto);
    }
}
