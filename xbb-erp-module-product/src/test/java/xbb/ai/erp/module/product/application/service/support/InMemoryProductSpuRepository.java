package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductSpuRepository implements ProductSpuRepository {

    private final List<ProductSpu> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);
    private final List<String> operationLog = new ArrayList<>();

    public void seed(ProductSpu productSpu) {
        data.add(productSpu);
        sequence.updateAndGet(current -> Math.max(current, productSpu.getId() == null ? current : productSpu.getId() + 1));
    }

    public List<ProductSpu> all() {
        return data;
    }

    public List<String> operationLog() {
        return operationLog;
    }

    @Override
    public Long save(ProductSpu productSpu, String userId) {
        if (productSpu.getId() == null) {
            productSpu.setId(sequence.getAndIncrement());
        }
        data.add(productSpu);
        return productSpu.getId();
    }

    @Override
    public void update(ProductSpu productSpu, String userId) {
        removeById(productSpu.getCorpid(), productSpu.getId(), userId);
        data.add(productSpu);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        operationLog.add("spu:" + id);
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
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
