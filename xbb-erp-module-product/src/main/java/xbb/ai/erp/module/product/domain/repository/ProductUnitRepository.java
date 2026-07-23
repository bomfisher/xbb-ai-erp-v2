package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.domain.model.ProductUnit;

import java.util.List;
import java.util.Map;

public interface ProductUnitRepository {

    Long save(ProductUnit productUnit, String userId);

    void update(ProductUnit productUnit, String userId);

    void removeById(String corpid, Long id, String userId);

    ProductUnit findById(String corpid, Long id);

    List<ProductUnit> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
