package xbb.ai.erp.module.settlement.infrastructure.persistence.convertor;

import xbb.ai.erp.module.settlement.domain.model.Payable;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PayablePO;

public final class PayableConvertor {

    private PayableConvertor() {
    }

    public static PayablePO toPO(Payable payable) {
        if (payable == null) {
            return null;
        }
        PayablePO po = new PayablePO();
        po.setId(payable.getId());
        po.setCorpid(payable.getCorpid());
        po.setPayableNo(payable.getPayableNo());
        po.setSupplierId(payable.getSupplierId());
        po.setSourceType(payable.getSourceType());
        po.setSourceInvoiceId(payable.getSourceInvoiceId());
        po.setPayableDate(payable.getPayableDate());
        po.setDueDate(payable.getDueDate());
        po.setAmount(payable.getAmount());
        po.setWrittenOffAmount(payable.getWrittenOffAmount());
        po.setRemainingAmount(payable.getRemainingAmount());
        po.setStatus(payable.getStatus());
        po.setAuditStatus(payable.getAuditStatus());
        po.setRemark(payable.getRemark());
        po.setCreatorId(payable.getCreatorId());
        po.setModifyId(payable.getModifyId());
        return po;
    }

    public static Payable toDomain(PayablePO po) {
        if (po == null) {
            return null;
        }
        Payable payable = new Payable();
        payable.setId(po.getId());
        payable.setCorpid(po.getCorpid());
        payable.setPayableNo(po.getPayableNo());
        payable.setSupplierId(po.getSupplierId());
        payable.setSourceType(po.getSourceType());
        payable.setSourceInvoiceId(po.getSourceInvoiceId());
        payable.setPayableDate(po.getPayableDate());
        payable.setDueDate(po.getDueDate());
        payable.setAmount(po.getAmount());
        payable.setWrittenOffAmount(po.getWrittenOffAmount());
        payable.setRemainingAmount(po.getRemainingAmount());
        payable.setStatus(po.getStatus());
        payable.setAuditStatus(po.getAuditStatus());
        payable.setRemark(po.getRemark());
        payable.setCreatorId(po.getCreatorId());
        payable.setModifyId(po.getModifyId());
        return payable;
    }
}
