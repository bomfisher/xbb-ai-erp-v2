package xbb.ai.erp.module.product.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.application.service.ProductAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductAdminAppService productAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductListItemVO>> list(@RequestBody ProductListDTO dto) {
        return ResultVO.success(productAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ProductSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<ProductDraftSaveVO> saveDraft(@RequestBody ProductDraftSaveDTO dto) {
        return ResultVO.success(productAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ProductSubmitSaveDTO dto) {
        return ResultVO.success(productAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<ProductDraftListItemVO>> draftList(@RequestBody ProductDraftListDTO dto) {
        return ResultVO.success(productAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<ProductDraftDetailVO> loadDraft(@RequestBody ProductDraftLoadDTO dto) {
        return ResultVO.success(productAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<ProductBusinessSelectOptionVO>> businessSelectQuickSearch(@RequestBody ProductBusinessSelectQueryDTO dto) {
        return ResultVO.success(productAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<ProductBusinessSelectOptionVO>> businessSelectDialogSearch(@RequestBody ProductBusinessSelectQueryDTO dto) {
        return ResultVO.success(productAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<ProductBusinessSelectOptionVO> businessSelectGetById(@RequestBody ProductBusinessSelectQueryDTO dto) {
        return ResultVO.success(productAdminAppService.businessSelectGetById(dto));
    }

    @PostMapping("/detail")
    public ResultVO<ProductDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<BaseVO> delete(@RequestBody BatchBaseDTO dto) {
        productAdminAppService.delete(dto);
        return ResultVO.success(new BaseVO());
    }
}
