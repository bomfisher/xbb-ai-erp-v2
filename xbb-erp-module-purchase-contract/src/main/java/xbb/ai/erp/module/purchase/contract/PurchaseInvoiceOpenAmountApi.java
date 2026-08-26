package xbb.ai.erp.module.purchase.contract;

import java.math.BigDecimal;

public interface PurchaseInvoiceOpenAmountApi {

    PurchaseInvoiceOpenAmount findOpenAmount(String corpid, Long invoiceId);

    void changePayableOpenedAmount(String corpid, Long invoiceId, BigDecimal delta, String userId);

    record PurchaseInvoiceOpenAmount(Long invoiceId, Long supplierId, BigDecimal invoiceAmount,
                                     BigDecimal openedAmount, BigDecimal availableAmount,
                                     String status, Integer auditStatus) {
    }
}
