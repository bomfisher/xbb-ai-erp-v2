package xbb.ai.erp.module.settlement.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;

import java.util.List;

public interface PaymentAdminAppService {
    ListBaseVO<PaymentListItemVO> list(ListBaseDTO dto);
    ListBaseVO<PaymentListItemVO> listAdvancePayment(ListBaseDTO dto);

    SaveItemVO<PaymentSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<PaymentSaveItemVO> addAdvancePayment(BaseDTO dto);

    SaveItemVO<PaymentSaveItemVO> updateItem(IdBaseDTO dto);
    SaveItemVO<PaymentSaveItemVO> updateAdvancePayment(IdBaseDTO dto);

    DraftSaveVO saveDraft(PaymentDraftSaveDTO dto);
    DraftSaveVO saveSupplierPaymentDraft(PaymentDraftSaveDTO dto);
    DraftSaveVO saveAdvancePaymentDraft(PaymentDraftSaveDTO dto);

    BaseVO saveAndSubmit(PaymentSubmitSaveDTO dto);
    BaseVO saveAdvancePayment(PaymentSubmitSaveDTO dto);

    List<PaymentDraftListItemVO> draftList(PaymentDraftListDTO dto);
    List<PaymentDraftListItemVO> supplierPaymentDraftList(PaymentDraftListDTO dto);
    List<PaymentDraftListItemVO> advancePaymentDraftList(PaymentDraftListDTO dto);

    PaymentDraftDetailVO loadDraft(PaymentDraftLoadDTO dto);
    PaymentDraftDetailVO loadSupplierPaymentDraft(PaymentDraftLoadDTO dto);
    PaymentDraftDetailVO loadAdvancePaymentDraft(PaymentDraftLoadDTO dto);

    Long save(PaymentSaveDTO dto);

    PaymentDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
    BaseVO audit(IdBaseDTO dto);
    BaseVO unaudit(IdBaseDTO dto);
    BaseVO auditSupplierPayment(IdBaseDTO dto);
    BaseVO unauditSupplierPayment(IdBaseDTO dto);
    BaseVO auditAdvancePayment(IdBaseDTO dto);
    BaseVO unauditAdvancePayment(IdBaseDTO dto);
}
