package xbb.ai.erp.module.settlement.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.ReceivableWriteOffStatusEnum;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.settlement.application.assembler.ReceivableAdminAssembler;
import xbb.ai.erp.module.settlement.application.validator.ReceivableValidator;
import xbb.ai.erp.module.settlement.application.port.ReceivableDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveProtocolValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveBusinessValidator;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;

@Service
@RequiredArgsConstructor
public class ReceivableSaveAppServiceImpl {

    private final ReceivableRepository receivableRepository;
    private final SalesInvoiceOpenAmountApi salesInvoiceOpenAmountApi;
    private final BizNoGenerator bizNoGenerator;

    @Transactional
    public BaseVO audit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable entity = receivableRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new BizException("应收开放项不存在");
        if (!AuditStatusEnum.PENDING.getCode().equals(entity.getAuditStatus())) throw new BizException("当前应收开放项不可审核");
        entity.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        entity.setModifyId(dto.getUserId());
        receivableRepository.update(entity);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable entity = receivableRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) throw new BizException("应收开放项不存在");
        if (!AuditStatusEnum.APPROVED.getCode().equals(entity.getAuditStatus())) throw new BizException("当前应收开放项不可反审核");
        entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        entity.setModifyId(dto.getUserId());
        receivableRepository.update(entity);
        return new BaseVO();
    }

    private final ReceivableDraftRepository draftRepository;
    private final ReceivableSaveProtocolValidator protocolValidator;
    private final ReceivableSaveCommonValidator commonValidator;
    private final ReceivableSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(ReceivableSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    @Transactional
    public Long save(ReceivableSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        ReceivableValidator.validateSave(dto);
        Receivable entity = ReceivableAdminAssembler.toReceivable(dto);
        if (entity.getId() == null) {
            validateInvoiceSource(entity);
            changeInvoiceOpenedAmount(entity, entity.getAmount(), dto.getUserId());
            entity.setWrittenOffAmount(BigDecimal.ZERO);
            entity.setRemainingAmount(entity.getAmount());
            entity.setStatus(ReceivableWriteOffStatusEnum.UNWRITTEN_OFF.getValue());
            entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            return receivableRepository.insert(entity);
        }
        Receivable existing = receivableRepository.findById(dto.getCorpid(), entity.getId());
        if (existing == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("应收开放项不存在");
        }
        entity.setReceivableNo(existing.getReceivableNo());
        validateInvoiceSource(entity);
        if (entity.getAmount() == null || entity.getAmount().compareTo(existing.getWrittenOffAmount()) < 0) {
            throw new BizException("应收金额不能小于已核销金额");
        }
        changeInvoiceOpenedAmount(existing, existing.getAmount().negate(), dto.getUserId());
        changeInvoiceOpenedAmount(entity, entity.getAmount(), dto.getUserId());
        entity.setWrittenOffAmount(existing.getWrittenOffAmount());
        entity.setRemainingAmount(entity.getAmount().subtract(existing.getWrittenOffAmount()));
        entity.setStatus(existing.getStatus());
        entity.setAuditStatus(existing.getAuditStatus());
        receivableRepository.update(entity);
        return entity.getId();
    }

    @Transactional
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            for (Long id : dto.getIdList()) {
                Receivable receivable = receivableRepository.findById(dto.getCorpid(), id);
                if (receivable == null) {
                    throw new BizException("应收开放项不存在");
                }
                changeInvoiceOpenedAmount(receivable, receivable.getAmount().negate(), dto.getUserId());
            }
            receivableRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    @Transactional
    public BaseVO voidReceivable(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable receivable = requireActiveReceivable(dto);
        if (receivable.getWrittenOffAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BizException("已核销或部分核销的应收款请先冲销核销后再作废");
        }
        closeReceivable(receivable, dto.getUserId());
        return new BaseVO();
    }

    @Transactional
    public BaseVO redFlush(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable original = requireActiveReceivable(dto);
        if (original.getWrittenOffAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BizException("已核销或部分核销的应收款请先冲销核销后再红冲");
        }
        closeReceivable(original, dto.getUserId());
        Receivable credit = new Receivable();
        credit.setCorpid(dto.getCorpid());
        credit.setReceivableNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.RECEIVABLE.getCode()));
        credit.setCustomerId(original.getCustomerId());
        credit.setSourceType("RED_FLUSH");
        credit.setReceivableDate(System.currentTimeMillis());
        credit.setDueDate(original.getDueDate());
        credit.setAmount(original.getAmount().negate());
        credit.setWrittenOffAmount(BigDecimal.ZERO);
        credit.setRemainingAmount(BigDecimal.ZERO);
        credit.setStatus(ReceivableWriteOffStatusEnum.CLOSED.getValue());
        credit.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        credit.setRemark("红冲原应收款：" + original.getReceivableNo());
        credit.setCreatorId(dto.getUserId());
        credit.setModifyId(dto.getUserId());
        receivableRepository.insert(credit);
        return new BaseVO();
    }

    private Receivable requireActiveReceivable(IdBaseDTO dto) {
        Receivable receivable = receivableRepository.findById(dto.getCorpid(), dto.getId());
        if (receivable == null || ReceivableWriteOffStatusEnum.CLOSED.getValue() == receivable.getStatus()) {
            throw new BizException("应收款不存在或已关闭");
        }
        return receivable;
    }

    private void closeReceivable(Receivable receivable, String userId) {
        changeInvoiceOpenedAmount(receivable, receivable.getAmount().negate(), userId);
        receivable.setRemainingAmount(BigDecimal.ZERO);
        receivable.setStatus(ReceivableWriteOffStatusEnum.CLOSED.getValue());
        receivable.setModifyId(userId);
        receivableRepository.update(receivable);
    }

    private void validateInvoiceSource(Receivable receivable) {
        if (!"SALES_INVOICE".equals(receivable.getSourceType())) {
            return;
        }
        if (receivable.getSourceInvoiceId() == null || receivable.getAmount() == null
            || receivable.getAmount().signum() <= 0) {
            throw new BizException("来源销售发票和应收金额不能为空");
        }
        SalesInvoiceOpenAmountApi.SalesInvoiceOpenAmount invoice = salesInvoiceOpenAmountApi
            .findOpenAmount(receivable.getCorpid(), receivable.getSourceInvoiceId());
        if (!invoice.customerId().equals(receivable.getCustomerId())) {
            throw new BizException("应收客户必须与来源销售发票一致");
        }
    }

    private void changeInvoiceOpenedAmount(Receivable receivable, BigDecimal delta, String userId) {
        if ("SALES_INVOICE".equals(receivable.getSourceType())) {
            salesInvoiceOpenAmountApi.changeReceivableOpenedAmount(receivable.getCorpid(),
                receivable.getSourceInvoiceId(), delta, userId);
        }
    }
}
