package xbb.ai.erp.module.settlement.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import xbb.ai.erp.module.settlement.admin.PaymentTypeEnum;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;
import xbb.ai.erp.module.settlement.application.service.PaymentAdminAppService;
import xbb.ai.erp.module.settlement.application.service.draft.PaymentDraftAppService;
import xbb.ai.erp.module.settlement.application.service.query.PaymentQueryAppServiceImpl;
import xbb.ai.erp.module.settlement.application.service.save.PaymentSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentAdminAppServiceImpl implements PaymentAdminAppService {

    private final PaymentQueryAppServiceImpl queryService;
    private final PaymentSaveAppServiceImpl saveService;
    private final PaymentDraftAppService draftService;

    @Override
    public ListBaseVO<PaymentListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public ListBaseVO<PaymentListItemVO> listAdvancePayment(ListBaseDTO dto) {
        return queryService.listAdvancePayment(dto);
    }

    @Override
    public SaveItemVO<PaymentSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<PaymentSaveItemVO> addAdvancePayment(BaseDTO dto) {
        return queryService.addAdvancePayment(dto);
    }

    @Override
    public SaveItemVO<PaymentSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public SaveItemVO<PaymentSaveItemVO> updateAdvancePayment(IdBaseDTO dto) {
        return queryService.updateAdvancePayment(dto);
    }

    @Override
    public DraftSaveVO saveDraft(PaymentDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public DraftSaveVO saveSupplierPaymentDraft(PaymentDraftSaveDTO dto) {
        return draftService.saveDraftByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public DraftSaveVO saveAdvancePaymentDraft(PaymentDraftSaveDTO dto) {
        return draftService.saveDraftByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    @Override
    public BaseVO saveAndSubmit(PaymentSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public BaseVO saveAdvancePayment(PaymentSubmitSaveDTO dto) {
        return saveService.saveAdvancePayment(dto);
    }

    @Override
    public List<PaymentDraftListItemVO> draftList(PaymentDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public List<PaymentDraftListItemVO> supplierPaymentDraftList(PaymentDraftListDTO dto) {
        return draftService.draftListByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public List<PaymentDraftListItemVO> advancePaymentDraftList(PaymentDraftListDTO dto) {
        return draftService.draftListByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    @Override
    public PaymentDraftDetailVO loadDraft(PaymentDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public PaymentDraftDetailVO loadSupplierPaymentDraft(PaymentDraftLoadDTO dto) {
        return draftService.loadDraftByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public PaymentDraftDetailVO loadAdvancePaymentDraft(PaymentDraftLoadDTO dto) {
        return draftService.loadDraftByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    @Override
    public Long save(PaymentSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public PaymentDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) { return saveService.audit(dto); }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) { return saveService.unaudit(dto); }

    @Override
    public BaseVO auditSupplierPayment(IdBaseDTO dto) {
        return saveService.auditByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public BaseVO unauditSupplierPayment(IdBaseDTO dto) {
        return saveService.unauditByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public BaseVO auditAdvancePayment(IdBaseDTO dto) {
        return saveService.auditByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    @Override
    public BaseVO unauditAdvancePayment(IdBaseDTO dto) {
        return saveService.unauditByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }
}
