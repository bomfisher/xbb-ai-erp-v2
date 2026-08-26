package xbb.ai.erp.module.sales.infrastructure.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.sales.contract.InventorySalesQueryApi;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesOutboundMapper;

@Service
@RequiredArgsConstructor
public class InventorySalesQueryAdapter implements InventorySalesQueryApi {
    private final SalesOutboundMapper mapper;

    @Override
    public Map<Long, SourceDocumentReference> findOutboundByIds(String corpid, Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        return mapper.findByIds(corpid, ids).stream().collect(Collectors.toMap(item -> item.getId(),
            item -> new SourceDocumentReference(item.getId(), item.getOutboundNo(), item.getOutboundDate(),
                item.getAuditStatus())));
    }
}
