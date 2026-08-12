package xbb.ai.erp.module.purchase.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.purchase.application.provider.PurchaseInboundListMetaProvider;

@Component
@RequiredArgsConstructor
public class PurchaseInboundListSchemaProvider {
    private final PurchaseInboundListMetaProvider listMetaProvider;
    public Map<String, ListFilterMetaPojo> conditionMetaMap() { return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO()); }
}
