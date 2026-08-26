package xbb.ai.erp.module.settlement.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.settlement.application.provider.ReceiptListMetaProvider;

@Component
@RequiredArgsConstructor
public class ReceiptListSchemaProvider {
    private final ReceiptListMetaProvider listMetaProvider;
    public Map<String, ListFilterMetaPojo> conditionMetaMap() { return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO()); }
}
