package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;

import java.util.List;
import java.util.Map;

public interface PurchaseInvoiceRepository {
    Long insert(PurchaseInvoice purchaseInvoice);

    void insertBatch(List<PurchaseInvoice> purchaseInvoiceList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseInvoice purchaseInvoice);

    PurchaseInvoice findById(String corpid, Long id);

    List<PurchaseInvoice> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
