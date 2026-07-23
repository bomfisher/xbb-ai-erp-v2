package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.product.admin.dto.ProductCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDeleteDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDetailDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSpuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSpuSkuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductSkuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductSpuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductSpuSkuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductVO;
import xbb.ai.erp.module.product.app.service.ProductAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductAppService productAppService;

    @PostMapping("/create")
    public ResultVO<Long> create(ProductCreateDTO dto) {
        return ResultVO.success(productAppService.create(dto));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> update(ProductUpdateDTO dto) {
        productAppService.update(dto);
        return ResultVO.success(Boolean.TRUE);
    }

    @PostMapping("/remove")
    public ResultVO<Boolean> remove(ProductDeleteDTO dto) {
        productAppService.remove(dto);
        return ResultVO.success(Boolean.TRUE);
    }

    @GetMapping("/detail")
    public ResultVO<ProductVO> detail(ProductDetailDTO dto) {
        return ResultVO.success(productAppService.detail(dto));
    }

    @GetMapping("/spu/list")
    public ResultVO<List<ProductSpuListVO>> listSpu(ProductSpuListDTO dto) {
        return ResultVO.success(productAppService.listSpu(dto));
    }

    @GetMapping("/sku/list")
    public ResultVO<List<ProductSkuListVO>> listSku(ProductSkuListDTO dto) {
        return ResultVO.success(productAppService.listSku(dto));
    }

    @GetMapping("/spu-sku/list")
    public ResultVO<List<ProductSpuSkuListVO>> listSpuSku(ProductSpuSkuListDTO dto) {
        return ResultVO.success(productAppService.listSpuSku(dto));
    }
}
