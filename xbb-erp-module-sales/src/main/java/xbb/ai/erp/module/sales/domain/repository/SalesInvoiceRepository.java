package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesInvoice;

import java.util.List;
import java.util.Map;

public interface SalesInvoiceRepository {
    Long insert(SalesInvoice salesInvoice);

    void insertBatch(List<SalesInvoice> salesInvoiceList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesInvoice salesInvoice);

    SalesInvoice findById(String corpid, Long id);

    List<SalesInvoice> findByIds(String corpid, java.util.Collection<Long> ids);

    List<SalesInvoice> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
