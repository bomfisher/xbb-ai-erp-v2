package xbb.ai.erp.module.sales.application.service.draft;

import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface SalesOrderDraftAppService {
    DraftSaveVO saveDraft(SalesOrderDraftSaveDTO dto);

    List<SalesOrderDraftListItemVO> draftList(SalesOrderDraftListDTO dto);

    SalesOrderDraftDetailVO loadDraft(SalesOrderDraftLoadDTO dto);
}
