package xbb.ai.erp.module.settlement.infrastructure.persistence.convertor;

import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptPO;

public final class ReceiptConvertor {

    private ReceiptConvertor() {
    }

    public static ReceiptPO toPO(Receipt receipt) {
        if (receipt == null) {
            return null;
        }
        ReceiptPO po = new ReceiptPO();
        po.setId(receipt.getId());
        po.setCorpid(receipt.getCorpid());
        po.setReceiptNo(receipt.getReceiptNo());
        po.setCustomerId(receipt.getCustomerId());
        po.setReceiptDate(receipt.getReceiptDate());
        po.setAmount(receipt.getAmount());
        po.setWrittenOffAmount(receipt.getWrittenOffAmount());
        po.setRemainingAmount(receipt.getRemainingAmount());
        po.setReceiptType(receipt.getReceiptType());
        po.setPaymentMethod(receipt.getPaymentMethod());
        po.setBankAccountId(receipt.getBankAccountId());
        po.setBankTransactionNo(receipt.getBankTransactionNo());
        po.setStatus(receipt.getStatus());
        po.setAuditStatus(receipt.getAuditStatus());
        po.setRemark(receipt.getRemark());
        po.setCreatorId(receipt.getCreatorId());
        po.setModifyId(receipt.getModifyId());
        return po;
    }

    public static Receipt toDomain(ReceiptPO po) {
        if (po == null) {
            return null;
        }
        Receipt receipt = new Receipt();
        receipt.setId(po.getId());
        receipt.setCorpid(po.getCorpid());
        receipt.setReceiptNo(po.getReceiptNo());
        receipt.setCustomerId(po.getCustomerId());
        receipt.setReceiptDate(po.getReceiptDate());
        receipt.setAmount(po.getAmount());
        receipt.setWrittenOffAmount(po.getWrittenOffAmount());
        receipt.setRemainingAmount(po.getRemainingAmount());
        receipt.setReceiptType(po.getReceiptType());
        receipt.setPaymentMethod(po.getPaymentMethod());
        receipt.setBankAccountId(po.getBankAccountId());
        receipt.setBankTransactionNo(po.getBankTransactionNo());
        receipt.setStatus(po.getStatus());
        receipt.setAuditStatus(po.getAuditStatus());
        receipt.setRemark(po.getRemark());
        receipt.setCreatorId(po.getCreatorId());
        receipt.setModifyId(po.getModifyId());
        return receipt;
    }
}
