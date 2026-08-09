package xbb.ai.erp.module.demo.sub.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;

@Component
@RequiredArgsConstructor
public class DemoSubListQueryAdapter {
  private final DemoSubListSchemaProvider schemaProvider;
  private final ListFilterConditionBuilder builder = new ListFilterConditionBuilder();

  public Map<String, Object> toConditionMap(ListBaseDTO dto) {
    return Map.of(
        "conditions",
        builder.build(dto.getConditions(), schemaProvider.conditionMetaMap()),
        "pageSize",
        dto.getPageSize(),
        "offset",
        dto.getOffset());
  }
}
