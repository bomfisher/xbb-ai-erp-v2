package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;

import java.util.List;
import java.util.Map;

public interface ProductSpuRepository {
    Long insert(ProductSpu productSpu);

    void insertBatch(List<ProductSpu> productSpuList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(ProductSpu productSpu);

    ProductSpu findById(String corpid, Long id);

    List<ProductSpu> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
