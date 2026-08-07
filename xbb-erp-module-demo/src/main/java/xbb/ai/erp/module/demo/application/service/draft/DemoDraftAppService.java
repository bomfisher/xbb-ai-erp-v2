package xbb.ai.erp.module.demo.application.service.draft;

import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftSaveVO;

import java.util.List;

public interface DemoDraftAppService {
    DemoDraftSaveVO saveDraft(DemoDraftSaveDTO dto);

    List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto);

    DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto);
}
