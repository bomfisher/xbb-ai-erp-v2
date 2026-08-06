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
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.application.service.ProductCategoryAdminAppService;

@RestController
@RequestMapping("/erp/v1/product/category")
@RequiredArgsConstructor
public class ProductCategoryAdminController {

    private final ProductCategoryAdminAppService productCategoryAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductCategoryVO>> list(@RequestBody ProductCategoryListDTO dto) {
        return ResultVO.success(productCategoryAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductCategorySaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productCategoryAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ProductCategorySaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productCategoryAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody ProductCategorySaveDTO dto) {
        return ResultVO.success(productCategoryAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ProductCategoryDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productCategoryAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        productCategoryAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
