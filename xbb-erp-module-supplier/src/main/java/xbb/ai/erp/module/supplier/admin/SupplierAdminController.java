package xbb.ai.erp.module.supplier.admin;

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
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftSaveVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.application.service.SupplierAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/supplier")
@RequiredArgsConstructor
public class SupplierAdminController {

    private final SupplierAdminAppService supplierAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SupplierListItemVO>> list(@RequestBody SupplierListDTO dto) {
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
    public ResultVO<SupplierDraftSaveVO> saveDraft(@RequestBody SupplierDraftSaveDTO dto) {
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

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<SupplierBusinessSelectOptionVO>> businessSelectQuickSearch(@RequestBody SupplierBusinessSelectQueryDTO dto) {
        return ResultVO.success(supplierAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<SupplierBusinessSelectOptionVO>> businessSelectDialogSearch(@RequestBody SupplierBusinessSelectQueryDTO dto) {
        return ResultVO.success(supplierAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<SupplierBusinessSelectOptionVO> businessSelectGetById(@RequestBody SupplierBusinessSelectQueryDTO dto) {
        return ResultVO.success(supplierAdminAppService.businessSelectGetById(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody SupplierSaveDTO dto) {
        return ResultVO.success(supplierAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<SupplierDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(supplierAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        supplierAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
