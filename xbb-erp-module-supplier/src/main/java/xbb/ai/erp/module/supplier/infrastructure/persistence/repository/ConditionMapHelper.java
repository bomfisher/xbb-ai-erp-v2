package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import java.util.Map;

public final class ConditionMapHelper {

    private ConditionMapHelper() {
    }

    public static Map<String, Object> normalizePage(Map<String, Object> conditionMap) {
        if (conditionMap == null) {
            return null;
        }
        Integer offset = toInteger(conditionMap.get("offset"));
        Integer pageSize = toInteger(conditionMap.get("pageSize"));
        if (offset != null && offset < 0) {
            offset = 0;
        }
        if (pageSize != null && pageSize <= 0) {
            pageSize = null;
        }
        conditionMap.put("offset", offset);
        conditionMap.put("pageSize", pageSize);
        return conditionMap;
    }

    private static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer integerValue) {
            return integerValue;
        }
        if (value instanceof Number numberValue) {
            return numberValue.intValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Integer.parseInt(stringValue);
        }
        return null;
    }
}
