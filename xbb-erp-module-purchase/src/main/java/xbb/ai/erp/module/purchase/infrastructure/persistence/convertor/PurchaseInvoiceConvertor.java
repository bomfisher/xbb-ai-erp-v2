package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoicePO;

public final class PurchaseInvoiceConvertor {

    private PurchaseInvoiceConvertor() {
    }

    public static PurchaseInvoicePO toPO(PurchaseInvoice purchaseInvoice) {
        if (purchaseInvoice == null) {
            return null;
        }
        PurchaseInvoicePO po = new PurchaseInvoicePO();
        po.setId(purchaseInvoice.getId());
        po.setCorpid(purchaseInvoice.getCorpid());
        po.setInvoiceNo(purchaseInvoice.getInvoiceNo());
        po.setSupplierInvoiceNo(purchaseInvoice.getSupplierInvoiceNo());
        po.setSupplierId(purchaseInvoice.getSupplierId());
        po.setInvoiceDate(purchaseInvoice.getInvoiceDate());
        po.setDueDate(purchaseInvoice.getDueDate());
        po.setPaymentTerm(purchaseInvoice.getPaymentTerm());
        po.setUntaxedAmount(purchaseInvoice.getUntaxedAmount());
        po.setTaxAmount(purchaseInvoice.getTaxAmount());
        po.setAmount(purchaseInvoice.getAmount());
        po.setPayableOpenedAmount(purchaseInvoice.getPayableOpenedAmount());
        po.setPayableAvailableAmount(purchaseInvoice.getPayableAvailableAmount());
        po.setInvoiceType(purchaseInvoice.getInvoiceType());
        po.setStatus(purchaseInvoice.getStatus());
        po.setAuditStatus(purchaseInvoice.getAuditStatus());
        po.setAuditTime(purchaseInvoice.getAuditTime());
        po.setPostedTime(purchaseInvoice.getPostedTime());
        po.setOriginalInvoiceId(purchaseInvoice.getOriginalInvoiceId());
        po.setRemark(purchaseInvoice.getRemark());
        po.setSourceType(purchaseInvoice.getSourceType());
        po.setSourceId(purchaseInvoice.getSourceId());
        po.setManualReason(purchaseInvoice.getManualReason());
        po.setCreatorId(purchaseInvoice.getCreatorId());
        po.setModifyId(purchaseInvoice.getModifyId());
        return po;
    }

    public static PurchaseInvoice toDomain(PurchaseInvoicePO po) {
        if (po == null) {
            return null;
        }
        PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
        purchaseInvoice.setId(po.getId());
        purchaseInvoice.setCorpid(po.getCorpid());
        purchaseInvoice.setInvoiceNo(po.getInvoiceNo());
        purchaseInvoice.setSupplierInvoiceNo(po.getSupplierInvoiceNo());
        purchaseInvoice.setSupplierId(po.getSupplierId());
        purchaseInvoice.setInvoiceDate(po.getInvoiceDate());
        purchaseInvoice.setDueDate(po.getDueDate());
        purchaseInvoice.setPaymentTerm(po.getPaymentTerm());
        purchaseInvoice.setUntaxedAmount(po.getUntaxedAmount());
        purchaseInvoice.setTaxAmount(po.getTaxAmount());
        purchaseInvoice.setAmount(po.getAmount());
        purchaseInvoice.setPayableOpenedAmount(po.getPayableOpenedAmount());
        purchaseInvoice.setPayableAvailableAmount(po.getPayableAvailableAmount());
        purchaseInvoice.setInvoiceType(po.getInvoiceType());
        purchaseInvoice.setStatus(po.getStatus());
        purchaseInvoice.setAuditStatus(po.getAuditStatus());
        purchaseInvoice.setAuditTime(po.getAuditTime());
        purchaseInvoice.setPostedTime(po.getPostedTime());
        purchaseInvoice.setOriginalInvoiceId(po.getOriginalInvoiceId());
        purchaseInvoice.setRemark(po.getRemark());
        purchaseInvoice.setSourceType(po.getSourceType());
        purchaseInvoice.setSourceId(po.getSourceId());
        purchaseInvoice.setManualReason(po.getManualReason());
        purchaseInvoice.setCreatorId(po.getCreatorId());
        purchaseInvoice.setModifyId(po.getModifyId());
        return purchaseInvoice;
    }
}
