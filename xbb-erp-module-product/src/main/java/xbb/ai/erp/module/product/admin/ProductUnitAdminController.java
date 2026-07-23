package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.app.service.ProductUnitAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product/unit")
@RequiredArgsConstructor
public class ProductUnitAdminController {

    private final ProductUnitAppService productUnitAppService;

    @PostMapping("/create")
    public ResultVO<Long> create(ProductUnitCreateDTO dto) {
        return ResultVO.success(productUnitAppService.create(dto));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> update(ProductUnitUpdateDTO dto) {
        productUnitAppService.update(dto);
        return ResultVO.success(Boolean.TRUE);
    }

    @PostMapping("/remove")
    public ResultVO<Boolean> remove(ProductUnitUpdateDTO dto) {
        productUnitAppService.remove(dto.getCorpid(), dto.getId(), dto.getUserId());
        return ResultVO.success(Boolean.TRUE);
    }

    @GetMapping("/detail")
    public ResultVO<ProductUnitVO> detail(ProductUnitUpdateDTO dto) {
        return ResultVO.success(productUnitAppService.detail(dto.getCorpid(), dto.getId()));
    }

    @GetMapping("/list")
    public ResultVO<List<ProductUnitVO>> list(ProductUnitListDTO dto) {
        return ResultVO.success(productUnitAppService.list(dto));
    }
}
