package xbb.ai.erp.module.settlement.contract;

import java.math.BigDecimal;

public record InvoiceReceivableCommand(String corpid, Long invoiceId, Long customerId, Long receivableDate,
                                      Long dueDate, BigDecimal amount, String remark, String userId) {
}
