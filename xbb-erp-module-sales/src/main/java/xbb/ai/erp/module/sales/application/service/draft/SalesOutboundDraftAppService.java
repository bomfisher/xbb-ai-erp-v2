package xbb.ai.erp.module.sales.application.service.draft;

import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface SalesOutboundDraftAppService {
    DraftSaveVO saveDraft(SalesOutboundDraftSaveDTO dto);

    List<SalesOutboundDraftListItemVO> draftList(SalesOutboundDraftListDTO dto);

    SalesOutboundDraftDetailVO loadDraft(SalesOutboundDraftLoadDTO dto);
}
