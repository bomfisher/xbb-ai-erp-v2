package xbb.ai.erp.module.demo.application.schema;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

import java.util.HashMap;
import java.util.Map;

@Component
public class DemoListQueryAdapter {
    private final DemoListSchemaProvider schemaProvider;
    private final ListFilterConditionBuilder conditionBuilder = new ListFilterConditionBuilder();

    public DemoListQueryAdapter(DemoListSchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public Map<String, Object> toConditionMap(ListBaseDTO dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("corpid", dto.getCorpid());
        map.put("keyword", dto.getKeyword());
        map.put("conditions", conditionBuilder.build(dto.getConditions(), schemaProvider.conditionMetaMap()));
        map.put("pageNum", dto.getPageNum());
        map.put("pageSize", dto.getPageSize());
        return map;
    }
}
