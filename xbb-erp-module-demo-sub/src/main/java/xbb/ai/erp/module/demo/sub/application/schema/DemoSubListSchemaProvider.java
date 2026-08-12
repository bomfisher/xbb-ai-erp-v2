package xbb.ai.erp.module.demo.sub.application.schema;

import java.util.Map;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.demo.sub.application.provider.DemoSubListMetaProvider;

@Component
public class DemoSubListSchemaProvider {
  public Map<String, ListFilterMetaPojo> conditionMetaMap() {
    return DemoSubListMetaProvider.conditionMetaMap();
  }
}
