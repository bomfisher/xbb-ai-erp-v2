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
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.application.service.ProductUnitAdminAppService;

@RestController
@RequestMapping("/erp/v1/product/unit")
@RequiredArgsConstructor
public class ProductUnitAdminController {

    private final ProductUnitAdminAppService productUnitAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductUnitVO>> list(@RequestBody ProductUnitListDTO dto) {
        return ResultVO.success(productUnitAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductUnitSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productUnitAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ProductUnitSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productUnitAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody ProductUnitSaveDTO dto) {
        return ResultVO.success(productUnitAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ProductUnitDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productUnitAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        productUnitAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
