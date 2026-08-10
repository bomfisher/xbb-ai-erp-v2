package xbb.ai.erp.module.common.application.util;

import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;

import java.util.HashMap;
import java.util.Map;

public class ListQueryMapUtil {

    private final ListFilterConditionBuilder builder = new ListFilterConditionBuilder();


    public Map<String, Object> gen(ListBaseDTO dto, Map<String, ListFilterMetaPojo> metaMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("corpid", dto.getCorpid());
        map.put("conditions", builder.build(dto.getConditions(), metaMap));
        map.put("offset", dto.getOffset());
        map.put("pageSize", dto.getPageSize());
        return map;
    }
}
