package xbb.ai.erp.module.masterdata.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.service.ProductSelectAppService;

@RestController
@RequestMapping("/erp/v1/product/businessSelect")
@RequiredArgsConstructor
public class ProductSelectAdminController {

    private final ProductSelectAppService productSelectAppService;

    @PostMapping("/quickSearch")
    public ResultVO<List<ProductSelectOptionVO>> quickSearch(@RequestBody ProductSelectQueryDTO dto) {
        return ResultVO.success(productSelectAppService.quickSearch(dto));
    }

    @PostMapping("/dialogSearch")
    public ResultVO<ListBaseVO<ProductSelectOptionVO>> dialogSearch(@RequestBody ProductSelectQueryDTO dto) {
        return ResultVO.success(productSelectAppService.dialogSearch(dto));
    }

    @PostMapping("/getById")
    public ResultVO<ProductSelectOptionVO> getById(@RequestBody ProductSelectQueryDTO dto) {
        return ResultVO.success(productSelectAppService.getById(dto));
    }
}
