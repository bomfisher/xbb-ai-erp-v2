package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;

import java.util.List;
import java.util.Map;

public interface SalesInvoiceLineRepository {
    Long insert(SalesInvoiceLine salesInvoiceLine);

    void insertBatch(List<SalesInvoiceLine> salesInvoiceLineList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesInvoiceLine salesInvoiceLine);

    SalesInvoiceLine findById(String corpid, Long id);

    List<SalesInvoiceLine> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
