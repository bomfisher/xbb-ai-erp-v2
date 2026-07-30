package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import xbb.ai.erp.base.common.support.QueryConditionMapHelper;

import java.util.Map;

final class ConditionMapHelper {

    private ConditionMapHelper() {
    }

    static Map<String, Object> prepare(Map<String, Object> source) {
        return QueryConditionMapHelper.prepare(source);
    }
}
