package xbb.ai.erp.module.settlement.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableBusinessSelectQueryDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableBusinessSelectOptionVO;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftDetailVO;

import java.util.List;

public interface ReceivableAdminAppService {
    ListBaseVO<ReceivableListItemVO> list(ListBaseDTO dto);

    SaveItemVO<ReceivableSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ReceivableSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(ReceivableDraftSaveDTO dto);

    BaseVO saveAndSubmit(ReceivableSubmitSaveDTO dto);

    List<ReceivableDraftListItemVO> draftList(ReceivableDraftListDTO dto);

    ReceivableDraftDetailVO loadDraft(ReceivableDraftLoadDTO dto);

    Long save(ReceivableSaveDTO dto);

    ReceivableDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    List<ReceivableBusinessSelectOptionVO> businessSelectQuickSearch(ReceivableBusinessSelectQueryDTO dto);

    ListBaseVO<ReceivableBusinessSelectOptionVO> businessSelectDialogSearch(ReceivableBusinessSelectQueryDTO dto);

    ReceivableBusinessSelectOptionVO businessSelectGetById(ReceivableBusinessSelectQueryDTO dto);

    SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    BaseVO voidReceivable(IdBaseDTO dto);

    BaseVO redFlush(IdBaseDTO dto);
}
