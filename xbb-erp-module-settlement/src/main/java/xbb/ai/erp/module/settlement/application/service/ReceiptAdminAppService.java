package xbb.ai.erp.module.settlement.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;

import java.util.List;

public interface ReceiptAdminAppService {
    ListBaseVO<ReceiptListItemVO> list(ListBaseDTO dto);

    ListBaseVO<ReceiptListItemVO> listAdvanceReceipt(ListBaseDTO dto);

    SaveItemVO<ReceiptSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ReceiptSaveItemVO> addAdvanceReceipt(BaseDTO dto);

    SaveItemVO<ReceiptSaveItemVO> updateItem(IdBaseDTO dto);

    SaveItemVO<ReceiptSaveItemVO> updateAdvanceReceipt(IdBaseDTO dto);

    DraftSaveVO saveDraft(ReceiptDraftSaveDTO dto);

    BaseVO saveAndSubmit(ReceiptSubmitSaveDTO dto);

    BaseVO saveAdvanceReceipt(ReceiptSubmitSaveDTO dto);

    List<ReceiptDraftListItemVO> draftList(ReceiptDraftListDTO dto);

    ReceiptDraftDetailVO loadDraft(ReceiptDraftLoadDTO dto);

    Long save(ReceiptSaveDTO dto);

    ReceiptDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    BaseVO writeOff(ReceiptWriteOffDTO dto);
    BaseVO audit(IdBaseDTO dto);
    BaseVO unaudit(IdBaseDTO dto);

}
