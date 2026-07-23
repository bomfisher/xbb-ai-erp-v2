package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.domain.model.ProductBrand;

import java.util.List;
import java.util.Map;

public interface ProductBrandRepository {

    Long save(ProductBrand productBrand, String userId);

    void update(ProductBrand productBrand, String userId);

    void removeById(String corpid, Long id, String userId);

    ProductBrand findById(String corpid, Long id);

    List<ProductBrand> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
