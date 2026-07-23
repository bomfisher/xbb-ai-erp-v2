package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.app.service.ProductCategoryAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product/category")
@RequiredArgsConstructor
public class ProductCategoryAdminController {

    private final ProductCategoryAppService productCategoryAppService;

    @PostMapping("/create")
    public ResultVO<Long> create(ProductCategoryCreateDTO dto) {
        return ResultVO.success(productCategoryAppService.create(dto));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> update(ProductCategoryUpdateDTO dto) {
        productCategoryAppService.update(dto);
        return ResultVO.success(Boolean.TRUE);
    }

    @PostMapping("/remove")
    public ResultVO<Boolean> remove(ProductCategoryUpdateDTO dto) {
        productCategoryAppService.remove(dto.getCorpid(), dto.getId(), dto.getUserId());
        return ResultVO.success(Boolean.TRUE);
    }

    @GetMapping("/detail")
    public ResultVO<ProductCategoryVO> detail(ProductCategoryUpdateDTO dto) {
        return ResultVO.success(productCategoryAppService.detail(dto.getCorpid(), dto.getId()));
    }

    @GetMapping("/list")
    public ResultVO<List<ProductCategoryVO>> list(ProductCategoryListDTO dto) {
        return ResultVO.success(productCategoryAppService.list(dto));
    }
}
