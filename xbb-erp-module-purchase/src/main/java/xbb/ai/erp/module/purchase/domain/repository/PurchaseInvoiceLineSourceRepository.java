package xbb.ai.erp.module.purchase.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;

public interface PurchaseInvoiceLineSourceRepository {
    void insertBatch(List<PurchaseInvoiceLineSource> sources);
    void removeByInvoiceLineIds(String corpid, List<Long> lineIds);
    List<PurchaseInvoiceLineSource> findByInvoiceLineIds(String corpid, List<Long> lineIds);

    BigDecimal sumInvoiceQuantityByPurchaseOrderItem(String corpid, Long purchaseOrderItemId);
}
