package xbb.ai.erp.module.settlement.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSubmitSaveDTO;
import xbb.ai.erp.module.settlement.application.assembler.ReceiptAdminAssembler;
import xbb.ai.erp.module.settlement.application.validator.ReceiptValidator;
import xbb.ai.erp.module.settlement.application.port.ReceiptDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.ReceiptSaveProtocolValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceiptSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceiptSaveBusinessValidator;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.application.service.ReceiptWriteOffService;

@Service
@RequiredArgsConstructor
public class ReceiptSaveAppServiceImpl {

    private final ReceiptRepository receiptRepository;

    private final ReceiptDraftRepository draftRepository;
    private final ReceiptSaveProtocolValidator protocolValidator;
    private final ReceiptSaveCommonValidator commonValidator;
    private final ReceiptSaveBusinessValidator businessValidator;
    private final ReceiptWriteOffService receiptWriteOffService;

    @Transactional
    public BaseVO audit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receipt entity = receiptRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new xbb.ai.erp.base.common.exception.BizException("收款单不存在");
        if (!AuditStatusEnum.PENDING.getCode().equals(entity.getAuditStatus())) throw new xbb.ai.erp.base.common.exception.BizException("当前收款单不可审核");
        entity.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        entity.setModifyId(dto.getUserId());
        receiptRepository.update(entity);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receipt entity = receiptRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new xbb.ai.erp.base.common.exception.BizException("收款单不存在");
        if (!AuditStatusEnum.APPROVED.getCode().equals(entity.getAuditStatus())) throw new xbb.ai.erp.base.common.exception.BizException("当前收款单不可反审核");
        entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        entity.setModifyId(dto.getUserId());
        receiptRepository.update(entity);
        return new BaseVO();
    }

    @Transactional
    public BaseVO saveAndSubmit(ReceiptSubmitSaveDTO dto) {
        return saveByReceiptType(dto, "CUSTOMER_PAYMENT");
    }

    @Transactional
    public BaseVO saveAdvanceReceipt(ReceiptSubmitSaveDTO dto) {
        return saveByReceiptType(dto, "ADVANCE_PAYMENT");
    }

    private BaseVO saveByReceiptType(ReceiptSubmitSaveDTO dto, String receiptType) {
        dto.getMain().setReceiptType(receiptType);
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        if (dto.getMain().getId() != null) {
        }
        Long receiptId = save(dto);
        if ("CUSTOMER_PAYMENT".equals(receiptType)) {
            receiptWriteOffService.writeOff(dto.getCorpid(), dto.getUserId(), receiptId, dto.getWriteOffs());
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(ReceiptSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        ReceiptValidator.validateSave(dto);
        Receipt entity = ReceiptAdminAssembler.toReceipt(dto);
        normalizePaymentDefaults(entity.getPayments());
        BigDecimal amount = entity.getPayments().stream()
            .map(xbb.ai.erp.module.settlement.domain.model.ReceiptPayment::getAmount)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        entity.setAmount(amount);
        if (entity.getId() == null) {
            entity.setWrittenOffAmount(BigDecimal.ZERO);
            entity.setRemainingAmount(entity.getAmount());
            entity.setStatus(0);
            entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            Long receiptId = receiptRepository.insert(entity);
            receiptRepository.replacePayments(dto.getCorpid(), receiptId, entity.getPayments());
            return receiptId;
        }
        Receipt existing = receiptRepository.findById(dto.getCorpid(), entity.getId());
        if (existing == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("收款单不存在");
        }
        entity.setWrittenOffAmount(existing.getWrittenOffAmount());
        entity.setRemainingAmount(entity.getAmount());
        entity.setStatus(existing.getStatus());
        receiptRepository.update(entity);
        receiptRepository.replacePayments(dto.getCorpid(), entity.getId(), entity.getPayments());
        return entity.getId();
    }

    private void normalizePaymentDefaults(List<xbb.ai.erp.module.settlement.domain.model.ReceiptPayment> payments) {
        payments.forEach(payment -> {
            if (payment.getHandlingFee() == null) {
                payment.setHandlingFee(BigDecimal.ZERO);
            }
            if (payment.getTransactionNo() == null) {
                payment.setTransactionNo("");
            }
            if (payment.getRemark() == null) {
                payment.setRemark("");
            }
        });
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            for (Long id : dto.getIdList()) {
                Receipt receipt = receiptRepository.findById(dto.getCorpid(), id);
                if (receipt == null) {
                    throw new xbb.ai.erp.base.common.exception.BizException("收款单不存在");
                }
                if (receipt.getWrittenOffAmount() != null && receipt.getWrittenOffAmount().compareTo(BigDecimal.ZERO) > 0) {
                    throw new xbb.ai.erp.base.common.exception.BizException("已发生核销的收款单不允许删除");
                }
            }
            receiptRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
