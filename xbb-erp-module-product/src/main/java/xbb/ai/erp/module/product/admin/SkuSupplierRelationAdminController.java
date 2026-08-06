package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO;
import xbb.ai.erp.module.product.application.service.SkuSupplierRelationAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product/sku-supplier-relation")
@RequiredArgsConstructor
public class SkuSupplierRelationAdminController {

    private final SkuSupplierRelationAdminAppService skuSupplierRelationAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SkuSupplierRelationListItemVO>> list(@RequestBody SkuSupplierRelationListDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<SkuSupplierRelationMainDTO>> addItem(@RequestBody SkuSupplierRelationQueryDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<SkuSupplierRelationMainDTO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody SkuSupplierRelationSaveDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.save(dto));
    }

    @PostMapping("/setDefault")
    public ResultVO<BaseVO> setDefault(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.setDefault(dto));
    }

    @PostMapping("/enable")
    public ResultVO<BaseVO> enable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.enable(dto));
    }

    @PostMapping("/disable")
    public ResultVO<BaseVO> disable(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.disable(dto));
    }

    @PostMapping("/delete")
    public ResultVO<BaseVO> delete(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.delete(dto));
    }

    @PostMapping("/history")
    public ResultVO<List<SkuSupplierRelationHistoryItemVO>> history(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.history(dto));
    }

    @PostMapping("/skuOptionsBySpu")
    public ResultVO<List<SkuSupplierRelationSkuOptionVO>> skuOptionsBySpu(@RequestBody SkuSupplierRelationSkuOptionsDTO dto) {
        return ResultVO.success(skuSupplierRelationAdminAppService.skuOptionsBySpu(dto));
    }
}
