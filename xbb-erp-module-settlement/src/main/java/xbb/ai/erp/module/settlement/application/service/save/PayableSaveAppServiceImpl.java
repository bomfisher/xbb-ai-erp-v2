package xbb.ai.erp.module.settlement.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.ReceivableWriteOffStatusEnum;
import xbb.ai.erp.module.settlement.application.assembler.PayableAdminAssembler;
import xbb.ai.erp.module.settlement.application.validator.PayableValidator;
import xbb.ai.erp.module.settlement.application.port.PayableDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.PayableSaveProtocolValidator;
import xbb.ai.erp.module.settlement.application.validator.PayableSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.PayableSaveBusinessValidator;
import xbb.ai.erp.module.settlement.domain.model.Payable;
import xbb.ai.erp.module.settlement.domain.repository.PayableRepository;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.contract.PurchaseInvoiceOpenAmountApi;

@Service
@RequiredArgsConstructor
public class PayableSaveAppServiceImpl {

    private final PayableRepository payableRepository;
    private final PurchaseInvoiceOpenAmountApi purchaseInvoiceOpenAmountApi;

    @Transactional
    public BaseVO audit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable entity = payableRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new BizException("应付开放项不存在");
        if (!AuditStatusEnum.PENDING.getCode().equals(entity.getAuditStatus())) throw new BizException("当前应付开放项不可审核");
        entity.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        entity.setModifyId(dto.getUserId());
        payableRepository.update(entity);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable entity = payableRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new BizException("应付开放项不存在");
        if (!AuditStatusEnum.APPROVED.getCode().equals(entity.getAuditStatus())) throw new BizException("当前应付开放项不可反审核");
        entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        entity.setModifyId(dto.getUserId());
        payableRepository.update(entity);
        return new BaseVO();
    }
    private final BizNoGenerator bizNoGenerator;

    private final PayableDraftRepository draftRepository;
    private final PayableSaveProtocolValidator protocolValidator;
    private final PayableSaveCommonValidator commonValidator;
    private final PayableSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(PayableSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    @Transactional
    public Long save(PayableSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PayableValidator.validateSave(dto);
        Payable entity = PayableAdminAssembler.toPayable(dto);
        if (entity.getId() == null) {
            validateInvoiceSource(entity);
            changeInvoiceOpenedAmount(entity, entity.getAmount(), dto.getUserId());
            initializeNewPayable(entity, dto.getCorpid());
            return payableRepository.insert(entity);
        }
        Payable existing = payableRepository.findById(dto.getCorpid(), entity.getId());
        if (existing == null) {
            throw new BizException("应付开放项不存在");
        }
        validateInvoiceSource(entity);
        if (entity.getAmount() == null || entity.getAmount().compareTo(existing.getWrittenOffAmount()) < 0) {
            throw new BizException("应付金额不能小于已核销金额");
        }
        changeInvoiceOpenedAmount(existing, existing.getAmount().negate(), dto.getUserId());
        changeInvoiceOpenedAmount(entity, entity.getAmount(), dto.getUserId());
        entity.setPayableNo(existing.getPayableNo());
        entity.setWrittenOffAmount(existing.getWrittenOffAmount());
        entity.setRemainingAmount(entity.getAmount().subtract(existing.getWrittenOffAmount()));
        entity.setStatus(existing.getStatus());
        entity.setAuditStatus(existing.getAuditStatus());
        payableRepository.update(entity);
        return entity.getId();
    }

    private void initializeNewPayable(Payable payable, String corpid) {
        if (payable.getPayableNo() == null || payable.getPayableNo().isBlank()) payable.setPayableNo(bizNoGenerator.next(corpid, BusinessCodeEnum.PAYABLE.getCode()));
        if (payable.getPayableDate() == null) payable.setPayableDate(System.currentTimeMillis());
        if (payable.getAmount() == null) payable.setAmount(BigDecimal.ZERO);
        if (payable.getWrittenOffAmount() == null) payable.setWrittenOffAmount(BigDecimal.ZERO);
        if (payable.getRemainingAmount() == null) payable.setRemainingAmount(payable.getAmount());
        if (payable.getStatus() == null) payable.setStatus(0);
        if (payable.getAuditStatus() == null) payable.setAuditStatus(0);
    }

    @Transactional
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            for (Long id : dto.getIdList()) {
                Payable payable = payableRepository.findById(dto.getCorpid(), id);
                if (payable == null) {
                    throw new BizException("应付开放项不存在");
                }
                changeInvoiceOpenedAmount(payable, payable.getAmount().negate(), dto.getUserId());
            }
            payableRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    @Transactional
    public BaseVO voidPayable(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable payable = requireActivePayable(dto);
        if (payable.getWrittenOffAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BizException("已核销或部分核销的应付款请先冲销核销后再作废");
        }
        closePayable(payable, dto.getUserId());
        return new BaseVO();
    }

    @Transactional
    public BaseVO redFlush(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable original = requireActivePayable(dto);
        if (original.getWrittenOffAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BizException("已核销或部分核销的应付款请先冲销核销后再红冲");
        }
        closePayable(original, dto.getUserId());
        Payable credit = new Payable();
        credit.setCorpid(dto.getCorpid());
        credit.setPayableNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PAYABLE.getCode()));
        credit.setSupplierId(original.getSupplierId());
        credit.setSourceType("RED_FLUSH");
        credit.setPayableDate(System.currentTimeMillis());
        credit.setDueDate(original.getDueDate());
        credit.setAmount(original.getAmount().negate());
        credit.setWrittenOffAmount(BigDecimal.ZERO);
        credit.setRemainingAmount(BigDecimal.ZERO);
        credit.setStatus(ReceivableWriteOffStatusEnum.CLOSED.getValue());
        credit.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        credit.setRemark("红冲原应付款：" + original.getPayableNo());
        credit.setCreatorId(dto.getUserId());
        credit.setModifyId(dto.getUserId());
        payableRepository.insert(credit);
        return new BaseVO();
    }

    private Payable requireActivePayable(IdBaseDTO dto) {
        Payable payable = payableRepository.findById(dto.getCorpid(), dto.getId());
        if (payable == null || Integer.valueOf(ReceivableWriteOffStatusEnum.CLOSED.getValue()).equals(payable.getStatus())) {
            throw new BizException("应付款不存在或已关闭");
        }
        return payable;
    }

    private void closePayable(Payable payable, String userId) {
        changeInvoiceOpenedAmount(payable, payable.getAmount().negate(), userId);
        payable.setRemainingAmount(BigDecimal.ZERO);
        payable.setStatus(ReceivableWriteOffStatusEnum.CLOSED.getValue());
        payable.setModifyId(userId);
        payableRepository.update(payable);
    }

    private void validateInvoiceSource(Payable payable) {
        if (!"PURCHASE_INVOICE".equals(payable.getSourceType())) {
            return;
        }
        if (payable.getSourceInvoiceId() == null || payable.getAmount() == null
            || payable.getAmount().signum() <= 0) {
            throw new BizException("来源采购发票和应付金额不能为空");
        }
        PurchaseInvoiceOpenAmountApi.PurchaseInvoiceOpenAmount invoice = purchaseInvoiceOpenAmountApi
            .findOpenAmount(payable.getCorpid(), payable.getSourceInvoiceId());
        if (!invoice.supplierId().equals(payable.getSupplierId())) {
            throw new BizException("应付供应商必须与来源采购发票一致");
        }
    }

    private void changeInvoiceOpenedAmount(Payable payable, BigDecimal delta, String userId) {
        if ("PURCHASE_INVOICE".equals(payable.getSourceType())) {
            purchaseInvoiceOpenAmountApi.changePayableOpenedAmount(payable.getCorpid(),
                payable.getSourceInvoiceId(), delta, userId);
        }
    }
}
