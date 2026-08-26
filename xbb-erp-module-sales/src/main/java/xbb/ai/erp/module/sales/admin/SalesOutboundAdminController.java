package xbb.ai.erp.module.sales.admin;

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
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSelectionFillDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSelectionFillVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSourceProductQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSourceProductOptionVO;
import java.util.List;
import xbb.ai.erp.module.sales.application.service.SalesOutboundAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/sales/salesOutbound")
@RequiredArgsConstructor
public class SalesOutboundAdminController {

    private final SalesOutboundAdminAppService salesOutboundAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SalesOutboundListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<SalesOutboundSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<SalesOutboundSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.updateItem(dto));
    }

    @PostMapping("/selectionFill")
    public ResultVO<SalesOutboundSelectionFillVO> selectionFill(@RequestBody SalesOutboundSelectionFillDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.selectionFill(dto));
    }

    @PostMapping("/sourceProductSelect/quickSearch")
    public ResultVO<List<SalesOutboundSourceProductOptionVO>> sourceProductQuickSearch(
        @RequestBody SalesOutboundSourceProductQueryDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.sourceProductQuickSearch(dto));
    }

    @PostMapping("/sourceProductSelect/dialogSearch")
    public ResultVO<ListBaseVO<SalesOutboundSourceProductOptionVO>> sourceProductDialogSearch(
        @RequestBody SalesOutboundSourceProductQueryDTO dto) {
        List<SalesOutboundSourceProductOptionVO> options = salesOutboundAdminAppService.sourceProductQuickSearch(dto);
        ListBaseVO<SalesOutboundSourceProductOptionVO> result = new ListBaseVO<>();
        result.setList(options);
        result.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), options.size()));
        return ResultVO.success(result);
    }

    @PostMapping("/sourceProductSelect/getById")
    public ResultVO<SalesOutboundSourceProductOptionVO> sourceProductGetById(
        @RequestBody SalesOutboundSourceProductQueryDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.sourceProductQuickSearch(dto).stream().findFirst().orElse(null));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody SalesOutboundDraftSaveDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody SalesOutboundSubmitSaveDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/audit")
    public ResultVO<BaseVO> audit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.audit(dto));
    }

    @PostMapping("/unaudit")
    public ResultVO<BaseVO> unaudit(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.unaudit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<SalesOutboundDraftListItemVO>> draftList(@RequestBody SalesOutboundDraftListDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<SalesOutboundDraftDetailVO> loadDraft(@RequestBody SalesOutboundDraftLoadDTO dto) {
        return ResultVO.success(salesOutboundAdminAppService.loadDraft(dto));
    }
}
