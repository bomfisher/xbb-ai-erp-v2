package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeProductSkuRepository implements ProductSkuRepository {

    private final List<ProductSku> data;

    public FakeProductSkuRepository(List<ProductSku> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public Long save(ProductSku productSku, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(ProductSku productSku, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBySpuId(String corpid, Long spuId, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductSku findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<ProductSku> findBySpuId(String corpid, Long spuId) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && spuId.equals(item.getSpuId())).toList();
    }

    @Override
    public List<ProductSku> findByCondition(Map<String, Object> condition) {
        Object corpid = condition.get("corpid");
        Object spuId = condition.get("spuId");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (spuId == null || spuId.equals(item.getSpuId()))
        ).toList();
    }

    @Override
    public long count(Map<String, Object> condition) {
        return findByCondition(condition).size();
    }
}
