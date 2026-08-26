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
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO;
import xbb.ai.erp.module.masterdata.application.service.FundAccountAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/masterData/fundAccount")
@RequiredArgsConstructor
public class FundAccountAdminController {

    private final FundAccountAdminAppService fundAccountAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<FundAccountListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<FundAccountSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<FundAccountSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody FundAccountDraftSaveDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody FundAccountSubmitSaveDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<FundAccountDraftListItemVO>> draftList(@RequestBody FundAccountDraftListDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<FundAccountDraftDetailVO> loadDraft(@RequestBody FundAccountDraftLoadDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<FundAccountBusinessSelectOptionVO>> businessSelectQuickSearch(
        @RequestBody FundAccountBusinessSelectQueryDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<FundAccountBusinessSelectOptionVO>> businessSelectDialogSearch(
        @RequestBody FundAccountBusinessSelectQueryDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<FundAccountBusinessSelectOptionVO> businessSelectGetById(
        @RequestBody FundAccountBusinessSelectQueryDTO dto) {
        return ResultVO.success(fundAccountAdminAppService.businessSelectGetById(dto));
    }
}
