package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.app.service.ProductBrandAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product/brand")
@RequiredArgsConstructor
public class ProductBrandAdminController {

    private final ProductBrandAppService productBrandAppService;

    @PostMapping("/create")
    public ResultVO<Long> create(ProductBrandCreateDTO dto) {
        return ResultVO.success(productBrandAppService.create(dto));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> update(ProductBrandUpdateDTO dto) {
        productBrandAppService.update(dto);
        return ResultVO.success(Boolean.TRUE);
    }

    @PostMapping("/remove")
    public ResultVO<Boolean> remove(ProductBrandUpdateDTO dto) {
        productBrandAppService.remove(dto.getCorpid(), dto.getId(), dto.getUserId());
        return ResultVO.success(Boolean.TRUE);
    }

    @GetMapping("/detail")
    public ResultVO<ProductBrandVO> detail(ProductBrandUpdateDTO dto) {
        return ResultVO.success(productBrandAppService.detail(dto.getCorpid(), dto.getId()));
    }

    @GetMapping("/list")
    public ResultVO<List<ProductBrandVO>> list(ProductBrandListDTO dto) {
        return ResultVO.success(productBrandAppService.list(dto));
    }
}
