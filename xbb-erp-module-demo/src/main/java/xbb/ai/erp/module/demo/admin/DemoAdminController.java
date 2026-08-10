package xbb.ai.erp.module.demo.admin;

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
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoBusinessSelectQueryDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoBusinessSelectOptionVO;
import xbb.ai.erp.module.demo.application.service.DemoAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/demo")
@RequiredArgsConstructor
public class DemoAdminController {

    private final DemoAdminAppService demoAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<DemoListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(demoAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<DemoSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(demoAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<DemoSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(demoAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<xbb.ai.erp.base.common.vo.DraftSaveVO> saveDraft(@RequestBody DemoDraftSaveDTO dto) {
        return ResultVO.success(demoAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody DemoSubmitSaveDTO dto) {
        return ResultVO.success(demoAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<DemoDraftListItemVO>> draftList(@RequestBody DemoDraftListDTO dto) {
        return ResultVO.success(demoAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<DemoDraftDetailVO> loadDraft(@RequestBody DemoDraftLoadDTO dto) {
        return ResultVO.success(demoAdminAppService.loadDraft(dto));
    }

    @PostMapping("/businessSelect/quickSearch")
    public ResultVO<List<DemoBusinessSelectOptionVO>> businessSelectQuickSearch(
        @RequestBody DemoBusinessSelectQueryDTO dto) {
        return ResultVO.success(demoAdminAppService.businessSelectQuickSearch(dto));
    }

    @PostMapping("/businessSelect/dialogSearch")
    public ResultVO<ListBaseVO<DemoBusinessSelectOptionVO>> businessSelectDialogSearch(
        @RequestBody DemoBusinessSelectQueryDTO dto) {
        return ResultVO.success(demoAdminAppService.businessSelectDialogSearch(dto));
    }

    @PostMapping("/businessSelect/getById")
    public ResultVO<DemoBusinessSelectOptionVO> businessSelectGetById(
        @RequestBody DemoBusinessSelectQueryDTO dto) {
        return ResultVO.success(demoAdminAppService.businessSelectGetById(dto));
    }
}
