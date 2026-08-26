package xbb.ai.erp.module.settlement.application.service.draft;

import xbb.ai.erp.module.settlement.admin.dto.PayableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface PayableDraftAppService {
    DraftSaveVO saveDraft(PayableDraftSaveDTO dto);

    List<PayableDraftListItemVO> draftList(PayableDraftListDTO dto);

    PayableDraftDetailVO loadDraft(PayableDraftLoadDTO dto);
}
