package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import xbb.ai.erp.base.common.support.QueryConditionMapHelper;

import java.util.Map;

final class WarehouseConditionMapHelper {

    private WarehouseConditionMapHelper() {
    }

    static Map<String, Object> prepare(Map<String, Object> source) {
        return QueryConditionMapHelper.prepare(source);
    }
}
