package xbb.ai.erp.module.sales.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftDetailVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSelectionFillDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSelectionFillVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSourceProductQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSourceProductOptionVO;
import java.util.List;

import java.util.List;

public interface SalesOutboundAdminAppService {
    ListBaseVO<SalesOutboundListItemVO> list(ListBaseDTO dto);

    SaveItemVO<SalesOutboundSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SalesOutboundSaveItemVO> updateItem(IdBaseDTO dto);

    SalesOutboundSelectionFillVO selectionFill(SalesOutboundSelectionFillDTO dto);

    List<SalesOutboundSourceProductOptionVO> sourceProductQuickSearch(SalesOutboundSourceProductQueryDTO dto);

    DraftSaveVO saveDraft(SalesOutboundDraftSaveDTO dto);

    BaseVO saveAndSubmit(SalesOutboundSubmitSaveDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    List<SalesOutboundDraftListItemVO> draftList(SalesOutboundDraftListDTO dto);

    SalesOutboundDraftDetailVO loadDraft(SalesOutboundDraftLoadDTO dto);

    Long save(SalesOutboundSaveDTO dto);

    SalesOutboundDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
