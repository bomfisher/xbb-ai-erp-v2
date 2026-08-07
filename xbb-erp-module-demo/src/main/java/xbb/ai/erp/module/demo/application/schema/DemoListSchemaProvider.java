package xbb.ai.erp.module.demo.application.schema;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.demo.application.provider.DemoListMetaProvider;

import java.util.Map;

@Component
public class DemoListSchemaProvider {
    public Map<String, ListFilterMetaPojo> conditionMetaMap() {
        return DemoListMetaProvider.conditionMetaMap();
    }
}
