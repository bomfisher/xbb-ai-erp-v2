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
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.masterdata.application.service.SupplierAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/masterData/supplier")
@RequiredArgsConstructor
public class SupplierAdminController {

    private final SupplierAdminAppService supplierAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SupplierListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(supplierAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<SupplierSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(supplierAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<SupplierSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(supplierAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody SupplierDraftSaveDTO dto) {
        return ResultVO.success(supplierAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody SupplierSubmitSaveDTO dto) {
        return ResultVO.success(supplierAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<SupplierDraftListItemVO>> draftList(@RequestBody SupplierDraftListDTO dto) {
        return ResultVO.success(supplierAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<SupplierDraftDetailVO> loadDraft(@RequestBody SupplierDraftLoadDTO dto) {
        return ResultVO.success(supplierAdminAppService.loadDraft(dto));
    }
}
