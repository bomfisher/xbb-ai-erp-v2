package xbb.ai.erp.module.settlement.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.PaymentTypeEnum;
import xbb.ai.erp.module.settlement.application.assembler.PaymentAdminAssembler;
import xbb.ai.erp.module.settlement.application.validator.PaymentValidator;
import xbb.ai.erp.module.settlement.application.port.PaymentDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.PaymentSaveProtocolValidator;
import xbb.ai.erp.module.settlement.application.validator.PaymentSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.PaymentSaveBusinessValidator;
import xbb.ai.erp.module.settlement.application.service.PaymentWriteOffService;
import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.repository.PaymentRepository;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;

@Service
@RequiredArgsConstructor
public class PaymentSaveAppServiceImpl {

    private final PaymentRepository paymentRepository;
    private final BizNoGenerator bizNoGenerator;

    @Transactional
    public BaseVO audit(IdBaseDTO dto) {
        return auditByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Transactional
    public BaseVO auditByPaymentType(IdBaseDTO dto, PaymentTypeEnum paymentType) {
        AdminParamValidator.validateIdQuery(dto);
        Payment entity = paymentRepository.findById(dto.getCorpid(), dto.getId());
        validatePaymentType(entity, paymentType);
        if (!AuditStatusEnum.PENDING.getCode().equals(entity.getAuditStatus())) throw new xbb.ai.erp.base.common.exception.BizException("当前付款单不可审核");
        entity.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        if (PaymentTypeEnum.SUPPLIER_PAYMENT.equals(paymentType)
            && entity.getRemainingAmount() != null && entity.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
            Payment advancePayment = new Payment();
            advancePayment.setCorpid(entity.getCorpid());
            advancePayment.setPaymentNo(bizNoGenerator.next(entity.getCorpid(), BusinessCodeEnum.ADVANCE_PAYMENT.getCode()));
            advancePayment.setSupplierId(entity.getSupplierId());
            advancePayment.setPaymentDate(entity.getPaymentDate());
            advancePayment.setAmount(entity.getRemainingAmount());
            advancePayment.setWrittenOffAmount(BigDecimal.ZERO);
            advancePayment.setRemainingAmount(entity.getRemainingAmount());
            advancePayment.setPaymentType(PaymentTypeEnum.ADVANCE_PAYMENT.getCode());
            advancePayment.setStatus(0);
            advancePayment.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
            advancePayment.setRemark("由付款单 " + entity.getPaymentNo() + " 超额付款自动生成");
            advancePayment.setCreatorId(dto.getUserId());
            advancePayment.setModifyId(dto.getUserId());
            advancePayment.setDetails(java.util.List.of());
            paymentRepository.insert(advancePayment);
            entity.setRemainingAmount(BigDecimal.ZERO);
            entity.setStatus(2);
        }
        entity.setModifyId(dto.getUserId());
        paymentRepository.update(entity);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(IdBaseDTO dto) {
        return unauditByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Transactional
    public BaseVO unauditByPaymentType(IdBaseDTO dto, PaymentTypeEnum paymentType) {
        AdminParamValidator.validateIdQuery(dto);
        Payment entity = paymentRepository.findById(dto.getCorpid(), dto.getId());
        validatePaymentType(entity, paymentType);
        if (!AuditStatusEnum.APPROVED.getCode().equals(entity.getAuditStatus())) throw new xbb.ai.erp.base.common.exception.BizException("当前付款单不可反审核");
        entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        entity.setModifyId(dto.getUserId());
        paymentRepository.update(entity);
        return new BaseVO();
    }

    private final PaymentDraftRepository draftRepository;
    private final PaymentSaveProtocolValidator protocolValidator;
    private final PaymentSaveCommonValidator commonValidator;
    private final PaymentSaveBusinessValidator businessValidator;
    private final PaymentWriteOffService paymentWriteOffService;

    @Transactional
    public BaseVO saveAndSubmit(PaymentSubmitSaveDTO dto) {
        return saveAndSubmitByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Transactional
    public BaseVO saveAdvancePayment(PaymentSubmitSaveDTO dto) {
        return saveAndSubmitByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    private BaseVO saveAndSubmitByPaymentType(PaymentSubmitSaveDTO dto, PaymentTypeEnum paymentType) {
        if (dto.getMain() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款单主信息不能为空");
        }
        dto.getMain().setPaymentType(paymentType.getCode());
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        BigDecimal writeOffTotal = dto.getWriteOffs() == null ? BigDecimal.ZERO : dto.getWriteOffs().stream()
            .map(xbb.ai.erp.module.settlement.admin.dto.PaymentWriteOffItemDTO::getAmount)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (dto.getMain().getAmount() == null || dto.getMain().getAmount().compareTo(writeOffTotal) < 0) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款金额必须大于等于核销应付款金额");
        }
        if (PaymentTypeEnum.SUPPLIER_PAYMENT.equals(paymentType)
            && dto.getMain().getAmount().compareTo(writeOffTotal) > 0 && !Boolean.TRUE.equals(dto.getConfirmAdvancePayment())) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款金额大于核销金额，审核完成后将自动创建预付款，请确认后提交");
        }
        Long paymentId = save(dto);
        paymentWriteOffService.writeOffPayment(dto.getCorpid(), dto.getUserId(), paymentId,
            dto.getMain().getSupplierId(), dto.getWriteOffs());
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(PaymentSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PaymentValidator.validateSave(dto);
        Payment entity = PaymentAdminAssembler.toPayment(dto);
        if (entity.getId() == null) {
            initializeNewPayment(entity, dto.getCorpid());
            Long paymentId = paymentRepository.insert(entity);
            paymentRepository.replaceDetails(dto.getCorpid(), paymentId, entity.getDetails());
            return paymentId;
        }
        Payment existing = paymentRepository.findById(dto.getCorpid(), entity.getId());
        if (existing == null || !Objects.equals(existing.getPaymentType(), entity.getPaymentType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款单不存在或不属于当前业务");
        }
        paymentRepository.update(entity);
        paymentRepository.replaceDetails(dto.getCorpid(), entity.getId(), entity.getDetails());
        return entity.getId();
    }

    private void initializeNewPayment(Payment payment, String corpid) {
        if (payment.getPaymentNo() == null || payment.getPaymentNo().isBlank()) payment.setPaymentNo(bizNoGenerator.next(corpid, BusinessCodeEnum.PAYMENT.getCode()));
        if (payment.getPaymentDate() == null) payment.setPaymentDate(System.currentTimeMillis());
        if (payment.getAmount() == null) payment.setAmount(BigDecimal.ZERO);
        if (payment.getWrittenOffAmount() == null) payment.setWrittenOffAmount(BigDecimal.ZERO);
        if (payment.getRemainingAmount() == null) payment.setRemainingAmount(payment.getAmount());
        if (payment.getPaymentType() == null) payment.setPaymentType(PaymentTypeEnum.SUPPLIER_PAYMENT.getCode());
        if (payment.getStatus() == null) payment.setStatus(0);
        if (payment.getAuditStatus() == null) payment.setAuditStatus(AuditStatusEnum.PENDING.getCode());
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            paymentRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private void validatePaymentType(Payment payment, PaymentTypeEnum paymentType) {
        if (payment == null || !paymentType.getCode().equals(payment.getPaymentType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款单不存在或不属于当前业务");
        }
    }
}
