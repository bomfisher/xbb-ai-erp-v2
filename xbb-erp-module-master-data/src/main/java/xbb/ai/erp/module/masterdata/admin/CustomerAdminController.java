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
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.masterdata.application.service.CustomerAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/masterData/customer")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerAdminAppService customerAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<CustomerListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(customerAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(customerAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(customerAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody CustomerDraftSaveDTO dto) {
        return ResultVO.success(customerAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody CustomerSubmitSaveDTO dto) {
        return ResultVO.success(customerAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<CustomerDraftListItemVO>> draftList(@RequestBody CustomerDraftListDTO dto) {
        return ResultVO.success(customerAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<CustomerDraftDetailVO> loadDraft(@RequestBody CustomerDraftLoadDTO dto) {
        return ResultVO.success(customerAdminAppService.loadDraft(dto));
    }
}
