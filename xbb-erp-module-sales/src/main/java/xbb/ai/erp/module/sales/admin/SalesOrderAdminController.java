package xbb.ai.erp.module.sales.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderBusinessSelectQueryDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.application.service.SalesOrderAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/sales/salesOrder")
@RequiredArgsConstructor
public class SalesOrderAdminController {

    private final SalesOrderAdminAppService salesOrderAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SalesOrderListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<SalesOrderSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<SalesOrderSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody SalesOrderDraftSaveDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody SalesOrderSubmitSaveDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<SalesOrderDraftListItemVO>> draftList(@RequestBody SalesOrderDraftListDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<SalesOrderDraftDetailVO> loadDraft(@RequestBody SalesOrderDraftLoadDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<SalesOrderBusinessSelectOptionVO>> businessSelectQuickSearch(
        @RequestBody SalesOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<SalesOrderBusinessSelectOptionVO>> businessSelectDialogSearch(
        @RequestBody SalesOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<SalesOrderBusinessSelectOptionVO> businessSelectGetById(
        @RequestBody SalesOrderBusinessSelectQueryDTO dto) {
        return ResultVO.success(salesOrderAdminAppService.businessSelectGetById(dto));
    }
}
