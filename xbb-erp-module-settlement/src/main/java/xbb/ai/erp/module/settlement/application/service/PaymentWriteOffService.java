package xbb.ai.erp.module.settlement.application.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentWriteOffAllocationDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentWriteOffSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentWriteOffItemDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentWriteOffListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentWriteOffSourceVO;
import xbb.ai.erp.module.settlement.domain.model.Payable;
import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.model.PaymentWriteOff;
import xbb.ai.erp.module.settlement.domain.repository.PayableRepository;
import xbb.ai.erp.module.settlement.domain.repository.PaymentRepository;
import xbb.ai.erp.module.settlement.domain.repository.PaymentWriteOffRepository;

@Service
@RequiredArgsConstructor
public class PaymentWriteOffService {
    private static final int UNWRITTEN_OFF = 0;
    private static final int PARTIALLY_WRITTEN_OFF = 1;
    private static final int WRITTEN_OFF = 2;

    private final PaymentRepository paymentRepository;
    private final PayableRepository payableRepository;
    private final PaymentWriteOffRepository writeOffRepository;
    private final BizNoGenerator bizNoGenerator;

    @Transactional
    public BaseVO writeOff(PaymentWriteOffSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSupplierId() == null || dto.getAllocations() == null || dto.getAllocations().isEmpty()) {
            throw new BizException("供应商和核销明细不能为空");
        }
        String writeoffNo = dto.getWriteoffNo() == null || dto.getWriteoffNo().isBlank()
            ? bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PAYMENT_WRITEOFF.getCode()) : dto.getWriteoffNo();
        for (PaymentWriteOffAllocationDTO allocation : dto.getAllocations()) {
            apply(dto, writeoffNo, allocation);
        }
        return new BaseVO();
    }

    public void writeOffPayment(String corpid, String userId, Long paymentId, Long supplierId,
        java.util.List<PaymentWriteOffItemDTO> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        String writeoffNo = bizNoGenerator.next(corpid, BusinessCodeEnum.PAYMENT_WRITEOFF.getCode());
        for (PaymentWriteOffItemDTO item : items) {
            PaymentWriteOffAllocationDTO allocation = new PaymentWriteOffAllocationDTO();
            allocation.setPaymentId(paymentId);
            allocation.setPayableId(item.getPayableId());
            allocation.setAmount(item.getAmount());
            PaymentWriteOffSaveDTO dto = new PaymentWriteOffSaveDTO();
            dto.setCorpid(corpid);
            dto.setUserId(userId);
            dto.setSupplierId(supplierId);
            apply(dto, writeoffNo, allocation);
        }
    }

    public ListBaseVO<PaymentWriteOffListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("offset", Math.max(0, (dto.getPageNum() - 1) * dto.getPageSize()));
        conditionMap.put("limit", dto.getPageSize());
        ListBaseVO<PaymentWriteOffListItemVO> result = new ListBaseVO<>();
        result.setList(writeOffRepository.findByCondition(conditionMap).stream().map(this::toListItem).toList());
        result.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), writeOffRepository.count(conditionMap).intValue()));
        return result;
    }

    public java.util.List<PaymentWriteOffSourceVO> advanceSources(String corpid, Long supplierId) {
        if (corpid == null || corpid.isBlank() || supplierId == null) {
            throw new BizException("公司和供应商不能为空");
        }
        return paymentRepository.findByCondition(Map.of("corpid", corpid, "supplierId", supplierId,
            "paymentType", "ADVANCE_PAYMENT")).stream()
            .filter(payment -> payment.getRemainingAmount() != null && payment.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
            .map(this::toPaymentSource).toList();
    }

    public java.util.List<PaymentWriteOffSourceVO> payableSources(String corpid, Long supplierId) {
        if (corpid == null || corpid.isBlank() || supplierId == null) {
            throw new BizException("公司和供应商不能为空");
        }
        return payableRepository.findByCondition(Map.of("corpid", corpid, "supplierId", supplierId)).stream()
            .filter(payable -> payable.getRemainingAmount() != null && payable.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
            .map(this::toPayableSource).toList();
    }

    private PaymentWriteOffListItemVO toListItem(PaymentWriteOff source) {
        PaymentWriteOffListItemVO target = new PaymentWriteOffListItemVO();
        target.setId(source.getId());
        target.setWriteoffNo(source.getWriteoffNo());
        target.setPaymentId(source.getPaymentId());
        target.setPayableId(source.getPayableId());
        target.setWriteoffDate(source.getWriteoffDate());
        target.setAmount(source.getAmount() == null ? null : source.getAmount().toPlainString());
        target.setStatus(source.getStatus());
        target.setRemark(source.getRemark());
        return target;
    }

    private PaymentWriteOffSourceVO toPaymentSource(Payment payment) {
        PaymentWriteOffSourceVO source = new PaymentWriteOffSourceVO();
        source.setId(payment.getId());
        source.setCode(payment.getPaymentNo());
        source.setAmount(payment.getAmount());
        source.setWrittenOffAmount(payment.getWrittenOffAmount());
        source.setRemainingAmount(payment.getRemainingAmount());
        return source;
    }

    private PaymentWriteOffSourceVO toPayableSource(Payable payable) {
        PaymentWriteOffSourceVO source = new PaymentWriteOffSourceVO();
        source.setId(payable.getId());
        source.setCode(payable.getPayableNo());
        source.setAmount(payable.getAmount());
        source.setWrittenOffAmount(payable.getWrittenOffAmount());
        source.setRemainingAmount(payable.getRemainingAmount());
        return source;
    }

    @Transactional
    public BaseVO reverse(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PaymentWriteOff writeOff = writeOffRepository.findById(dto.getCorpid(), dto.getId());
        if (writeOff == null || !Integer.valueOf(1).equals(writeOff.getStatus())) {
            throw new BizException("核销记录不存在或已冲销");
        }
        Payment payment = requirePayment(dto.getCorpid(), writeOff.getPaymentId());
        Payable payable = requirePayable(dto.getCorpid(), writeOff.getPayableId());
        long reversedTime = System.currentTimeMillis();
        writeOffRepository.reverse(dto.getCorpid(), dto.getId(), reversedTime, dto.getUserId());
        restoreBalances(payment, payable, writeOff.getAmount(), dto.getUserId());
        return new BaseVO();
    }

    private void apply(PaymentWriteOffSaveDTO dto, String writeoffNo, PaymentWriteOffAllocationDTO allocation) {
        if (allocation.getPaymentId() == null || allocation.getPayableId() == null || allocation.getAmount() == null
                || allocation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("核销金额必须大于零，且付款和应付款不能为空");
        }
        Payment payment = requirePayment(dto.getCorpid(), allocation.getPaymentId());
        Payable payable = requirePayable(dto.getCorpid(), allocation.getPayableId());
        if (!"ADVANCE_PAYMENT".equals(payment.getPaymentType()) && !"SUPPLIER_PAYMENT".equals(payment.getPaymentType())) {
            throw new BizException("付款类型不支持核销应付款");
        }
        if (!dto.getSupplierId().equals(payment.getSupplierId()) || !dto.getSupplierId().equals(payable.getSupplierId())) {
            throw new BizException("付款和应付款必须属于所选供应商");
        }
        if (payment.getRemainingAmount().compareTo(allocation.getAmount()) < 0
                || payable.getRemainingAmount().compareTo(allocation.getAmount()) < 0) {
            throw new BizException("付款或应付款余额不足");
        }
        updateBalances(payment, payable, allocation.getAmount(), dto.getUserId());
        PaymentWriteOff writeOff = new PaymentWriteOff();
        writeOff.setCorpid(dto.getCorpid());
        writeOff.setSupplierId(dto.getSupplierId());
        writeOff.setWriteoffNo(writeoffNo);
        writeOff.setPaymentId(payment.getId());
        writeOff.setPayableId(payable.getId());
        writeOff.setWriteoffDate(dto.getWriteoffDate() == null ? System.currentTimeMillis() : dto.getWriteoffDate());
        writeOff.setAmount(allocation.getAmount());
        writeOff.setRemark(dto.getRemark());
        writeOff.setCreatorId(dto.getUserId());
        writeOff.setModifyId(dto.getUserId());
        writeOffRepository.insert(writeOff);
    }

    private void updateBalances(Payment payment, Payable payable, BigDecimal amount, String userId) {
        payment.setWrittenOffAmount(payment.getWrittenOffAmount().add(amount));
        payment.setRemainingAmount(payment.getRemainingAmount().subtract(amount));
        payment.setStatus(status(payment.getRemainingAmount(), payment.getAmount()));
        payment.setModifyId(userId);
        payable.setWrittenOffAmount(payable.getWrittenOffAmount().add(amount));
        payable.setRemainingAmount(payable.getRemainingAmount().subtract(amount));
        payable.setStatus(status(payable.getRemainingAmount(), payable.getAmount()));
        payable.setModifyId(userId);
        paymentRepository.update(payment);
        payableRepository.update(payable);
    }

    private void restoreBalances(Payment payment, Payable payable, BigDecimal amount, String userId) {
        payment.setWrittenOffAmount(payment.getWrittenOffAmount().subtract(amount));
        payment.setRemainingAmount(payment.getRemainingAmount().add(amount));
        payment.setStatus(status(payment.getRemainingAmount(), payment.getAmount()));
        payment.setModifyId(userId);
        payable.setWrittenOffAmount(payable.getWrittenOffAmount().subtract(amount));
        payable.setRemainingAmount(payable.getRemainingAmount().add(amount));
        payable.setStatus(status(payable.getRemainingAmount(), payable.getAmount()));
        payable.setModifyId(userId);
        paymentRepository.update(payment);
        payableRepository.update(payable);
    }

    private Payment requirePayment(String corpid, Long id) {
        Payment payment = paymentRepository.findById(corpid, id);
        if (payment == null) {
            throw new BizException("付款单不存在");
        }
        return payment;
    }

    private Payable requirePayable(String corpid, Long id) {
        Payable payable = payableRepository.findById(corpid, id);
        if (payable == null) {
            throw new BizException("应付款开放项不存在");
        }
        return payable;
    }

    private int status(BigDecimal remainingAmount, BigDecimal amount) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            return WRITTEN_OFF;
        }
        if (remainingAmount.compareTo(amount) < 0) {
            return PARTIALLY_WRITTEN_OFF;
        }
        return UNWRITTEN_OFF;
    }
}
