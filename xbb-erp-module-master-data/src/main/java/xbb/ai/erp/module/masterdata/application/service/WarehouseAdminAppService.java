package xbb.ai.erp.module.masterdata.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseBusinessSelectOptionVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftDetailVO;

import java.util.List;

public interface WarehouseAdminAppService {
    ListBaseVO<WarehouseListItemVO> list(ListBaseDTO dto);

    SaveItemVO<WarehouseSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<WarehouseSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(WarehouseDraftSaveDTO dto);

    BaseVO saveAndSubmit(WarehouseSubmitSaveDTO dto);

    List<WarehouseDraftListItemVO> draftList(WarehouseDraftListDTO dto);

    WarehouseDraftDetailVO loadDraft(WarehouseDraftLoadDTO dto);

    Long save(WarehouseSaveDTO dto);

    WarehouseDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    List<WarehouseBusinessSelectOptionVO> businessSelectQuickSearch(WarehouseBusinessSelectQueryDTO dto);

    ListBaseVO<WarehouseBusinessSelectOptionVO> businessSelectDialogSearch(WarehouseBusinessSelectQueryDTO dto);

    WarehouseBusinessSelectOptionVO businessSelectGetById(WarehouseBusinessSelectQueryDTO dto);
}
