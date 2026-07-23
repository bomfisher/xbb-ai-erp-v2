package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.domain.model.ProductCategory;

import java.util.List;
import java.util.Map;

public interface ProductCategoryRepository {

    Long save(ProductCategory productCategory, String userId);

    void update(ProductCategory productCategory, String userId);

    void removeById(String corpid, Long id, String userId);

    ProductCategory findById(String corpid, Long id);

    List<ProductCategory> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
