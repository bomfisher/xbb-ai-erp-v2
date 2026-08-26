package xbb.ai.erp.module.masterdata.application.service.draft;

import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface FundAccountDraftAppService {
    DraftSaveVO saveDraft(FundAccountDraftSaveDTO dto);

    List<FundAccountDraftListItemVO> draftList(FundAccountDraftListDTO dto);

    FundAccountDraftDetailVO loadDraft(FundAccountDraftLoadDTO dto);
}
