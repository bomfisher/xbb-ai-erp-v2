package xbb.ai.erp.module.masterdata.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO;
import xbb.ai.erp.module.masterdata.application.service.ProductSpuAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/masterData/productSpu")
@RequiredArgsConstructor
public class ProductSpuAdminController {

    private final ProductSpuAdminAppService productSpuAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductSpuListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(productSpuAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductSpuSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productSpuAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<ProductSpuSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(productSpuAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody ProductSpuDraftSaveDTO dto) {
        return ResultVO.success(productSpuAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody ProductSpuSubmitSaveDTO dto) {
        return ResultVO.success(productSpuAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<ProductSpuDraftListItemVO>> draftList(@RequestBody ProductSpuDraftListDTO dto) {
        return ResultVO.success(productSpuAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<ProductSpuDraftDetailVO> loadDraft(@RequestBody ProductSpuDraftLoadDTO dto) {
        return ResultVO.success(productSpuAdminAppService.loadDraft(dto));
    }
}
