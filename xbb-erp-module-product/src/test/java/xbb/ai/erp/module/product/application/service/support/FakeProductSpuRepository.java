package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeProductSpuRepository implements ProductSpuRepository {

    private final List<ProductSpu> data;

    public FakeProductSpuRepository(List<ProductSpu> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public Long save(ProductSpu productSpu, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(ProductSpu productSpu, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductSpu findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<ProductSpu> findByCondition(Map<String, Object> condition) {
        Object corpid = condition.get("corpid");
        Object keyword = condition.get("keyword");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (keyword == null
                || item.getSpuCode() != null && item.getSpuCode().contains(keyword.toString())
                || item.getSpuName() != null && item.getSpuName().contains(keyword.toString()))
        ).toList();
    }

    @Override
    public long count(Map<String, Object> condition) {
        return findByCondition(condition).size();
    }
}
