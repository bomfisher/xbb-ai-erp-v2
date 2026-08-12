package xbb.ai.erp.module.masterdata.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftDetailVO;
import xbb.ai.erp.module.masterdata.application.service.WarehouseAdminAppService;
import xbb.ai.erp.module.masterdata.application.service.draft.WarehouseDraftAppService;
import xbb.ai.erp.module.masterdata.application.service.query.WarehouseQueryAppServiceImpl;
import xbb.ai.erp.module.masterdata.application.service.save.WarehouseSaveAppServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseAdminAppServiceImpl implements WarehouseAdminAppService {

    private final WarehouseQueryAppServiceImpl queryService;
    private final WarehouseSaveAppServiceImpl saveService;
    private final WarehouseDraftAppService draftService;

    @Override
    public ListBaseVO<WarehouseListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<WarehouseSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<WarehouseSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(WarehouseDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(WarehouseSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<WarehouseDraftListItemVO> draftList(WarehouseDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public WarehouseDraftDetailVO loadDraft(WarehouseDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(WarehouseSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public WarehouseDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
