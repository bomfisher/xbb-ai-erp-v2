package xbb.ai.erp.module.sales.contract;

import java.math.BigDecimal;

public interface SalesInvoiceOpenAmountApi {

    SalesInvoiceOpenAmount findOpenAmount(String corpid, Long invoiceId);

    void changeReceivableOpenedAmount(String corpid, Long invoiceId, BigDecimal delta, String userId);

    record SalesInvoiceOpenAmount(Long invoiceId, Long customerId, BigDecimal invoiceAmount,
                                  BigDecimal openedAmount, BigDecimal availableAmount,
                                  String status, Integer auditStatus) {
    }
}
