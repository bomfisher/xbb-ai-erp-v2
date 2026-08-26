package xbb.ai.erp.module.settlement.infrastructure.persistence.convertor;

import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceivablePO;

public final class ReceivableConvertor {

    private ReceivableConvertor() {
    }

    public static ReceivablePO toPO(Receivable receivable) {
        if (receivable == null) {
            return null;
        }
        ReceivablePO po = new ReceivablePO();
        po.setId(receivable.getId());
        po.setCorpid(receivable.getCorpid());
        po.setReceivableNo(receivable.getReceivableNo());
        po.setCustomerId(receivable.getCustomerId());
        po.setSourceType(receivable.getSourceType());
        po.setSourceInvoiceId(receivable.getSourceInvoiceId());
        po.setOpeningBatchId(receivable.getOpeningBatchId());
        po.setReceivableDate(receivable.getReceivableDate());
        po.setDueDate(receivable.getDueDate());
        po.setAmount(receivable.getAmount());
        po.setWrittenOffAmount(receivable.getWrittenOffAmount());
        po.setRemainingAmount(receivable.getRemainingAmount());
        po.setStatus(receivable.getStatus());
        po.setAuditStatus(receivable.getAuditStatus());
        po.setRemark(receivable.getRemark());
        po.setCreatorId(receivable.getCreatorId());
        po.setModifyId(receivable.getModifyId());
        return po;
    }

    public static Receivable toDomain(ReceivablePO po) {
        if (po == null) {
            return null;
        }
        Receivable receivable = new Receivable();
        receivable.setId(po.getId());
        receivable.setCorpid(po.getCorpid());
        receivable.setReceivableNo(po.getReceivableNo());
        receivable.setCustomerId(po.getCustomerId());
        receivable.setSourceType(po.getSourceType());
        receivable.setSourceInvoiceId(po.getSourceInvoiceId());
        receivable.setOpeningBatchId(po.getOpeningBatchId());
        receivable.setReceivableDate(po.getReceivableDate());
        receivable.setDueDate(po.getDueDate());
        receivable.setAmount(po.getAmount());
        receivable.setWrittenOffAmount(po.getWrittenOffAmount());
        receivable.setRemainingAmount(po.getRemainingAmount());
        receivable.setStatus(po.getStatus());
        receivable.setAuditStatus(po.getAuditStatus());
        receivable.setRemark(po.getRemark());
        receivable.setCreatorId(po.getCreatorId());
        receivable.setModifyId(po.getModifyId());
        return receivable;
    }
}
