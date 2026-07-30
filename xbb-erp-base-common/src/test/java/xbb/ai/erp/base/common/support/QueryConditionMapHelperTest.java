package xbb.ai.erp.base.common.support;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryConditionMapHelperTest {

    @Test
    void should_filter_null_values_from_condition_map() {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", "corp-001");
        conditionMap.put("id", null);

        Map<String, Object> prepared = QueryConditionMapHelper.prepare(conditionMap);

        assertEquals("corp-001", prepared.get("corpid"));
        assertFalse(prepared.containsKey("id"));
    }

    @Test
    void should_build_offset_when_page_num_and_page_size_exist() {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("pageNum", 2);
        conditionMap.put("pageSize", 10);

        Map<String, Object> prepared = QueryConditionMapHelper.prepare(conditionMap);

        assertEquals(2, prepared.get("pageNum"));
        assertEquals(10, prepared.get("pageSize"));
        assertEquals(10, prepared.get("offset"));
    }

    @Test
    void should_keep_page_size_only_when_page_num_missing() {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("pageSize", 5);

        Map<String, Object> prepared = QueryConditionMapHelper.prepare(conditionMap);

        assertEquals(5, prepared.get("pageSize"));
        assertFalse(prepared.containsKey("pageNum"));
        assertFalse(prepared.containsKey("offset"));
    }

    @Test
    void should_trim_group_and_order_by_strings() {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("groupByStr", "  customer_id,   biz_status  ");
        conditionMap.put("orderByStr", "  update_time   desc  ");

        Map<String, Object> prepared = QueryConditionMapHelper.prepare(conditionMap);

        assertEquals("customer_id, biz_status", prepared.get("groupByStr"));
        assertEquals("update_time desc", prepared.get("orderByStr"));
    }

    @Test
    void should_reject_unsafe_group_or_order_by_strings() {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("orderByStr", "update_time desc; drop table customer");

        assertThrows(BizException.class, () -> QueryConditionMapHelper.prepare(conditionMap));
    }
}
