package xbb.ai.erp.module.settlement.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.settlement.application.provider.ReceivableListMetaProvider;

@Component
@RequiredArgsConstructor
public class ReceivableListSchemaProvider {
    private final ReceivableListMetaProvider listMetaProvider;
    public Map<String, ListFilterMetaPojo> conditionMetaMap() { return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO()); }
}
