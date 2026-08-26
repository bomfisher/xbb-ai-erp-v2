package xbb.ai.erp.module.settlement.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.PayableListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableSaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftDetailVO;

import java.util.List;

public interface PayableAdminAppService {
    ListBaseVO<PayableListItemVO> list(PayableListDTO dto);

    SaveItemVO<PayableSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PayableSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(PayableDraftSaveDTO dto);

    BaseVO saveAndSubmit(PayableSubmitSaveDTO dto);

    List<PayableDraftListItemVO> draftList(PayableDraftListDTO dto);

    PayableDraftDetailVO loadDraft(PayableDraftLoadDTO dto);

    Long save(PayableSaveDTO dto);

    PayableDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto);

    BaseVO audit(IdBaseDTO dto);

    BaseVO unaudit(IdBaseDTO dto);

    BaseVO voidPayable(IdBaseDTO dto);

    BaseVO redFlush(IdBaseDTO dto);
}
