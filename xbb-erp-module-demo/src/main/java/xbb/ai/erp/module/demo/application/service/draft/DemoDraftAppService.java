package xbb.ai.erp.module.demo.application.service.draft;

import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface DemoDraftAppService {
    DraftSaveVO saveDraft(DemoDraftSaveDTO dto);

    List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto);

    DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto);
}
