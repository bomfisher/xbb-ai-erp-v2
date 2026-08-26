package xbb.ai.erp.module.sales.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售合同审批所需的冻结查询快照。
 */
public record SalesContractApprovalSnapshot(
    Long contractId,
    String contractNo,
    Long customerId,
    BigDecimal totalAmount,
    String currencyCode,
    LocalDate signDate
) {
}
