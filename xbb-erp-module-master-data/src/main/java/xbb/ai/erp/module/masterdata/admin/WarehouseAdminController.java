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
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.masterdata.application.service.WarehouseAdminAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/masterData/warehouse")
@RequiredArgsConstructor
public class WarehouseAdminController {

    private final WarehouseAdminAppService warehouseAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<WarehouseListItemVO>> list(@RequestBody ListBaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<WarehouseSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<WarehouseSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(warehouseAdminAppService.updateItem(dto));
    }

    @PostMapping("/saveDraft")
    public ResultVO<DraftSaveVO> saveDraft(@RequestBody WarehouseDraftSaveDTO dto) {
        return ResultVO.success(warehouseAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody WarehouseSubmitSaveDTO dto) {
        return ResultVO.success(warehouseAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<WarehouseDraftListItemVO>> draftList(@RequestBody WarehouseDraftListDTO dto) {
        return ResultVO.success(warehouseAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<WarehouseDraftDetailVO> loadDraft(@RequestBody WarehouseDraftLoadDTO dto) {
        return ResultVO.success(warehouseAdminAppService.loadDraft(dto));
    }
}
