package xbb.ai.erp.module.product.admin;

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
import xbb.ai.erp.module.product.admin.dto.WarehouseListDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.product.application.service.WarehouseAdminAppService;

@RestController
@RequestMapping("/erp/v1/product/warehouse")
@RequiredArgsConstructor
public class WarehouseAdminController {

    private final WarehouseAdminAppService warehouseAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<WarehouseListItemVO> list(@RequestBody WarehouseListDTO dto) {
        return warehouseAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<WarehouseSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return warehouseAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<WarehouseSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return warehouseAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody WarehouseSaveDTO dto) {
        return warehouseAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public WarehouseDetailVO detail(@RequestBody IdBaseDTO dto) {
        return warehouseAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        warehouseAdminAppService.delete(dto);
    }
}
