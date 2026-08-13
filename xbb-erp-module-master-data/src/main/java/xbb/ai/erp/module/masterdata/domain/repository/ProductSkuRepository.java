package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.ProductSku;

import java.util.List;
import java.util.Map;

public interface ProductSkuRepository {
    Long insert(ProductSku productSku);

    void insertBatch(List<ProductSku> productSkuList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(ProductSku productSku);

    ProductSku findById(String corpid, Long id);

    List<ProductSku> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
