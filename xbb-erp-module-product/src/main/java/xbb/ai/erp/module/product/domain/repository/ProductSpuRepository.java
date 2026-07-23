package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.List;
import java.util.Map;

public interface ProductSpuRepository {

    Long save(ProductSpu productSpu, String userId);

    void update(ProductSpu productSpu, String userId);

    void removeById(String corpid, Long id, String userId);

    ProductSpu findById(String corpid, Long id);

    List<ProductSpu> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
