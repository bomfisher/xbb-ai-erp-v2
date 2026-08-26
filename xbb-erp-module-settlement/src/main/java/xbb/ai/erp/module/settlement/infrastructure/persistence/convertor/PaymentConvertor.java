package xbb.ai.erp.module.settlement.infrastructure.persistence.convertor;

import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentPO;

public final class PaymentConvertor {

    private PaymentConvertor() {
    }

    public static PaymentPO toPO(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentPO po = new PaymentPO();
        po.setId(payment.getId());
        po.setCorpid(payment.getCorpid());
        po.setPaymentNo(payment.getPaymentNo());
        po.setSupplierId(payment.getSupplierId());
        po.setPaymentDate(payment.getPaymentDate());
        po.setAmount(payment.getAmount());
        po.setWrittenOffAmount(payment.getWrittenOffAmount());
        po.setRemainingAmount(payment.getRemainingAmount());
        po.setPaymentType(payment.getPaymentType());
        po.setPaymentMethod(payment.getPaymentMethod());
        po.setBankAccountId(payment.getBankAccountId());
        po.setBankTransactionNo(payment.getBankTransactionNo());
        po.setStatus(payment.getStatus());
        po.setAuditStatus(payment.getAuditStatus());
        po.setRemark(payment.getRemark());
        po.setCreatorId(payment.getCreatorId());
        po.setModifyId(payment.getModifyId());
        return po;
    }

    public static Payment toDomain(PaymentPO po) {
        if (po == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(po.getId());
        payment.setCorpid(po.getCorpid());
        payment.setPaymentNo(po.getPaymentNo());
        payment.setSupplierId(po.getSupplierId());
        payment.setPaymentDate(po.getPaymentDate());
        payment.setAmount(po.getAmount());
        payment.setWrittenOffAmount(po.getWrittenOffAmount());
        payment.setRemainingAmount(po.getRemainingAmount());
        payment.setPaymentType(po.getPaymentType());
        payment.setPaymentMethod(po.getPaymentMethod());
        payment.setBankAccountId(po.getBankAccountId());
        payment.setBankTransactionNo(po.getBankTransactionNo());
        payment.setStatus(po.getStatus());
        payment.setAuditStatus(po.getAuditStatus());
        payment.setRemark(po.getRemark());
        payment.setCreatorId(po.getCreatorId());
        payment.setModifyId(po.getModifyId());
        return payment;
    }
}
