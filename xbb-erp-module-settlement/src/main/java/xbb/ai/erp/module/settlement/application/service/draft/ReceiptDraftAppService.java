package xbb.ai.erp.module.settlement.application.service.draft;

import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface ReceiptDraftAppService {
    DraftSaveVO saveDraft(ReceiptDraftSaveDTO dto);

    List<ReceiptDraftListItemVO> draftList(ReceiptDraftListDTO dto);

    ReceiptDraftDetailVO loadDraft(ReceiptDraftLoadDTO dto);
}
