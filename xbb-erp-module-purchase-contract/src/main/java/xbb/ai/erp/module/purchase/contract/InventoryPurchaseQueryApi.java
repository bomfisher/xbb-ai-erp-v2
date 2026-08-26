package xbb.ai.erp.module.purchase.contract;
import java.util.Collection; import java.util.Map;
public interface InventoryPurchaseQueryApi { Map<Long, SourceDocumentReference> findInboundByIds(String corpid, Collection<Long> ids); record SourceDocumentReference(Long id, String documentNo, Long documentDate, Integer auditStatus) {} }
