package xbb.ai.erp.module.settlement.application.service.draft;

import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface ReceivableDraftAppService {
    DraftSaveVO saveDraft(ReceivableDraftSaveDTO dto);

    List<ReceivableDraftListItemVO> draftList(ReceivableDraftListDTO dto);

    ReceivableDraftDetailVO loadDraft(ReceivableDraftLoadDTO dto);
}
