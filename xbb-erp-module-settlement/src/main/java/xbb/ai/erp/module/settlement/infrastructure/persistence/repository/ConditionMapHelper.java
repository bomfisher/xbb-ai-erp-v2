package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import xbb.ai.erp.base.common.exception.BizException;
import java.util.HashMap;
import java.util.Map;

final class ConditionMapHelper {

    private ConditionMapHelper() {
    }

    static Map<String, Object> prepare(Map<String, Object> source) {
        Map<String, Object> conditionMap = source == null ? new HashMap<>() : new HashMap<>(source);
        normalizePage(conditionMap);
        normalizeClause(conditionMap, "groupByStr");
        normalizeClause(conditionMap, "orderByStr");
        return conditionMap;
    }

    private static void normalizePage(Map<String, Object> conditionMap) {
        Integer pageSize = toInteger(conditionMap.get("pageSize"));
        Integer pageNum = toInteger(conditionMap.get("pageNum"));
        if (pageSize == null || pageSize <= 0) {
            conditionMap.remove("pageSize");
            conditionMap.remove("pageNum");
            conditionMap.remove("offset");
            return;
        }
        conditionMap.put("pageSize", pageSize);
        if (pageNum != null && pageNum > 0) {
            conditionMap.put("pageNum", pageNum);
            conditionMap.put("offset", (pageNum - 1) * pageSize);
            return;
        }
        conditionMap.remove("pageNum");
        conditionMap.remove("offset");
    }

    private static void normalizeClause(Map<String, Object> conditionMap, String key) {
        Object value = conditionMap.get(key);
        if (!(value instanceof String clause)) {
            conditionMap.remove(key);
            return;
        }
        String trimmed = clause.trim();
        if (trimmed.isEmpty()) {
            conditionMap.remove(key);
            return;
        }
        if (!trimmed.matches("[a-zA-Z0-9_,\\s]+")) {
            throw new BizException(key + " contains invalid characters");
        }
        conditionMap.put(key, trimmed.replaceAll("\\s+", " "));
    }

    private static Integer toInteger(Object value) {
        if (value instanceof Integer integerValue) {
            return integerValue;
        }
        if (value instanceof Long longValue) {
            return longValue.intValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Integer.parseInt(stringValue.trim());
        }
        return null;
    }
}
