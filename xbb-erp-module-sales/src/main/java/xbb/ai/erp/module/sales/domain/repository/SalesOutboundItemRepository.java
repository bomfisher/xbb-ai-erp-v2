package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;

import java.util.List;
import java.util.Map;

public interface SalesOutboundItemRepository {
    Long insert(SalesOutboundItem salesOutboundItem);

    void insertBatch(List<SalesOutboundItem> salesOutboundItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesOutboundItem salesOutboundItem);

    SalesOutboundItem findById(String corpid, Long id);

    List<SalesOutboundItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
