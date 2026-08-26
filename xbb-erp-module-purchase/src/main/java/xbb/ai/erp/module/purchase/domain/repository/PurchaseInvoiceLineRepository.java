package xbb.ai.erp.module.purchase.domain.repository;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;

public interface PurchaseInvoiceLineRepository {
    void insertBatch(List<PurchaseInvoiceLine> lines);
    void removeByInvoiceId(String corpid, Long invoiceId);
    List<PurchaseInvoiceLine> findByInvoiceId(String corpid, Long invoiceId);
}
