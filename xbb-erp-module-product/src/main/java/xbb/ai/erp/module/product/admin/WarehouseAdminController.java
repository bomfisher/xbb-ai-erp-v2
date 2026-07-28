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
import xbb.ai.erp.base.common.vo.ResultVO;
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
    public ResultVO<ListBaseVO<WarehouseListItemVO>> list(@RequestBody WarehouseListDTO dto) {
        return ResultVO.success(warehouseAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<WarehouseSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<WarehouseSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody WarehouseSaveDTO dto) {
        return ResultVO.success(warehouseAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<WarehouseDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        warehouseAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
