package xbb.ai.erp.module.sales.domain.repository;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;

public interface SalesInvoiceLineSourceRepository {
    void insertBatch(List<SalesInvoiceLineSource> sources);

    void removeByInvoiceLineIds(String corpid, List<Long> invoiceLineIds);

    List<SalesInvoiceLineSource> findByInvoiceLineIds(String corpid, List<Long> invoiceLineIds);

    List<SalesInvoiceLineSource> findByCondition(Map<String, Object> conditionMap);

    BigDecimal sumPostedQuantity(String corpid, String sourceType, Long sourceLineId, Long excludedInvoiceId);

    BigDecimal sumPostedQuantityBySalesOrderItem(String corpid, Long salesOrderItemId, Long excludedInvoiceId);
}
