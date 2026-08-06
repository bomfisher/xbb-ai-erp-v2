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
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.application.service.ProductBrandAdminAppService;

@RestController
@RequestMapping("/erp/v1/product/brand")
@RequiredArgsConstructor
public class ProductBrandAdminController {

    private final ProductBrandAdminAppService productBrandAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductBrandVO>> list(@RequestBody ProductBrandListDTO dto) {
        return ResultVO.success(productBrandAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductBrandSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productBrandAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ProductBrandSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productBrandAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody ProductBrandSaveDTO dto) {
        return ResultVO.success(productBrandAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ProductBrandDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productBrandAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        productBrandAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
