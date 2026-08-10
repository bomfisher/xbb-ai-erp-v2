package xbb.ai.erp.module.demo.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.demo.application.provider.DemoListMetaProvider;

@Component
@RequiredArgsConstructor
public class DemoListSchemaProvider {
    private final DemoListMetaProvider listMetaProvider;

    public Map<String, ListFilterMetaPojo> conditionMetaMap() {
        return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO());
    }
}
