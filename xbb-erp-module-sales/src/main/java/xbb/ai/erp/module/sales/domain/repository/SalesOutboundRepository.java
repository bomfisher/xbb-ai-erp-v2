package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesOutbound;

import java.util.List;
import java.util.Map;

public interface SalesOutboundRepository {
    Long insert(SalesOutbound salesOutbound);

    void insertBatch(List<SalesOutbound> salesOutboundList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesOutbound salesOutbound);

    SalesOutbound findById(String corpid, Long id);

    List<SalesOutbound> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
