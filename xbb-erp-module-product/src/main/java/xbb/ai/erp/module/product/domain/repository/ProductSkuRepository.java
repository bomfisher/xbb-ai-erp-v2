package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.admin.vo.ProductSpuSkuListVO;
import xbb.ai.erp.module.product.domain.model.ProductSku;

import java.util.List;
import java.util.Map;

public interface ProductSkuRepository {

    Long save(ProductSku productSku, String userId);

    void update(ProductSku productSku, String userId);

    void removeById(String corpid, Long id, String userId);

    void removeBySpuId(String corpid, Long spuId, String userId);

    ProductSku findById(String corpid, Long id);

    ProductSku findBySpuId(String corpid, Long spuId);

    List<ProductSku> findByCondition(Map<String, Object> condition);

    List<ProductSpuSkuListVO> findSpuSkuList(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
