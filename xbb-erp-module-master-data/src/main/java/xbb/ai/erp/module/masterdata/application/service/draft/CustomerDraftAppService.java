package xbb.ai.erp.module.masterdata.application.service.draft;

import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface CustomerDraftAppService {
    DraftSaveVO saveDraft(CustomerDraftSaveDTO dto);

    List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto);

    CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto);
}
