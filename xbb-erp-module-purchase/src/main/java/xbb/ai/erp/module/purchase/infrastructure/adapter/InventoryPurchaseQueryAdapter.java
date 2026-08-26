package xbb.ai.erp.module.purchase.infrastructure.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.purchase.contract.InventoryPurchaseQueryApi;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInboundMapper;

@Service
@RequiredArgsConstructor
public class InventoryPurchaseQueryAdapter implements InventoryPurchaseQueryApi {
    private final PurchaseInboundMapper mapper;

    @Override
    public Map<Long, SourceDocumentReference> findInboundByIds(String corpid, Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        return mapper.findByIds(corpid, ids)
            .stream()
            .collect(Collectors.toMap(item -> item.getId(), item -> new SourceDocumentReference(
                item.getId(), item.getInboundNo(), item.getInboundDate(), item.getAuditStatus())));
    }
}
