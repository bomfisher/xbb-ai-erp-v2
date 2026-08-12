package xbb.ai.erp.module.masterdata.application.service.draft;

import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface WarehouseDraftAppService {
    DraftSaveVO saveDraft(WarehouseDraftSaveDTO dto);

    List<WarehouseDraftListItemVO> draftList(WarehouseDraftListDTO dto);

    WarehouseDraftDetailVO loadDraft(WarehouseDraftLoadDTO dto);
}
