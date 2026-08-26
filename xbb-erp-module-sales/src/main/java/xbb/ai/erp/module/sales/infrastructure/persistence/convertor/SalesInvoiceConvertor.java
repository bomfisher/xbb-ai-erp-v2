package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoicePO;

public final class SalesInvoiceConvertor {

    private SalesInvoiceConvertor() {
    }

    public static SalesInvoicePO toPO(SalesInvoice salesInvoice) {
        if (salesInvoice == null) {
            return null;
        }
        SalesInvoicePO po = new SalesInvoicePO();
        po.setId(salesInvoice.getId());
        po.setCorpid(salesInvoice.getCorpid());
        po.setInvoiceNo(salesInvoice.getInvoiceNo());
        po.setCustomerId(salesInvoice.getCustomerId());
        po.setInvoiceDate(salesInvoice.getInvoiceDate());
        po.setDueDate(salesInvoice.getDueDate());
        po.setPaymentTerm(salesInvoice.getPaymentTerm());
        po.setUntaxedAmount(salesInvoice.getUntaxedAmount());
        po.setTaxAmount(salesInvoice.getTaxAmount());
        po.setAmount(salesInvoice.getAmount());
        po.setReceivableOpenedAmount(salesInvoice.getReceivableOpenedAmount());
        po.setReceivableAvailableAmount(salesInvoice.getReceivableAvailableAmount());
        po.setInvoiceType(salesInvoice.getInvoiceType());
        po.setOriginalInvoiceId(salesInvoice.getOriginalInvoiceId());
        po.setStatus(salesInvoice.getStatus());
        po.setAuditStatus(salesInvoice.getAuditStatus());
        po.setAuditTime(salesInvoice.getAuditTime());
        po.setPostedTime(salesInvoice.getPostedTime());
        po.setRemark(salesInvoice.getRemark());
        po.setCreatorId(salesInvoice.getCreatorId());
        po.setModifyId(salesInvoice.getModifyId());
        return po;
    }

    public static SalesInvoice toDomain(SalesInvoicePO po) {
        if (po == null) {
            return null;
        }
        SalesInvoice salesInvoice = new SalesInvoice();
        salesInvoice.setId(po.getId());
        salesInvoice.setCorpid(po.getCorpid());
        salesInvoice.setInvoiceNo(po.getInvoiceNo());
        salesInvoice.setCustomerId(po.getCustomerId());
        salesInvoice.setInvoiceDate(po.getInvoiceDate());
        salesInvoice.setDueDate(po.getDueDate());
        salesInvoice.setPaymentTerm(po.getPaymentTerm());
        salesInvoice.setUntaxedAmount(po.getUntaxedAmount());
        salesInvoice.setTaxAmount(po.getTaxAmount());
        salesInvoice.setAmount(po.getAmount());
        salesInvoice.setReceivableOpenedAmount(po.getReceivableOpenedAmount());
        salesInvoice.setReceivableAvailableAmount(po.getReceivableAvailableAmount());
        salesInvoice.setInvoiceType(po.getInvoiceType());
        salesInvoice.setOriginalInvoiceId(po.getOriginalInvoiceId());
        salesInvoice.setStatus(po.getStatus());
        salesInvoice.setAuditStatus(po.getAuditStatus());
        salesInvoice.setAuditTime(po.getAuditTime());
        salesInvoice.setPostedTime(po.getPostedTime());
        salesInvoice.setRemark(po.getRemark());
        salesInvoice.setCreatorId(po.getCreatorId());
        salesInvoice.setModifyId(po.getModifyId());
        return salesInvoice;
    }
}
