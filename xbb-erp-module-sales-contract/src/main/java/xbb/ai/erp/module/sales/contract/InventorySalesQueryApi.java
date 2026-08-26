package xbb.ai.erp.module.sales.contract;
import java.util.Collection; import java.util.Map;
public interface InventorySalesQueryApi { Map<Long, SourceDocumentReference> findOutboundByIds(String corpid, Collection<Long> ids); record SourceDocumentReference(Long id, String documentNo, Long documentDate, Integer auditStatus) {} }
