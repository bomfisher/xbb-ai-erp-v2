package xbb.ai.erp.module.supplier.admin;

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
import xbb.ai.erp.module.supplier.admin.dto.VendorListDTO;
import xbb.ai.erp.module.supplier.admin.dto.VendorSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.VendorDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorSaveItemVO;
import xbb.ai.erp.module.supplier.application.service.VendorAdminAppService;

@RestController
@RequestMapping("/erp/v1/supplier")
@RequiredArgsConstructor
public class VendorAdminController {

    private final VendorAdminAppService vendorAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<VendorListItemVO> list(@RequestBody VendorListDTO dto) {
        return vendorAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<VendorSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return vendorAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<VendorSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return vendorAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody VendorSaveDTO dto) {
        return vendorAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public VendorDetailVO detail(@RequestBody IdBaseDTO dto) {
        return vendorAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        vendorAdminAppService.delete(dto);
    }
}
