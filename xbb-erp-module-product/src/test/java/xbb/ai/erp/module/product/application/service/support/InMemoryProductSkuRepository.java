package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductSkuRepository implements ProductSkuRepository {

    private final List<ProductSku> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);
    private final List<String> operationLog = new ArrayList<>();

    public void seed(ProductSku productSku) {
        data.add(productSku);
        sequence.updateAndGet(current -> Math.max(current, productSku.getId() == null ? current : productSku.getId() + 1));
    }

    public List<ProductSku> all() {
        return data;
    }

    public List<String> operationLog() {
        return operationLog;
    }

    public List<String> operationLogWith(InMemoryProductSpuRepository spuRepository) {
        List<String> result = new ArrayList<>(operationLog);
        result.addAll(spuRepository.operationLog());
        return result;
    }

    @Override
    public Long save(ProductSku productSku, String userId) {
        if (productSku.getId() == null) {
            productSku.setId(sequence.getAndIncrement());
        }
        data.add(productSku);
        return productSku.getId();
    }

    @Override
    public void update(ProductSku productSku, String userId) {
        removeById(productSku.getCorpid(), productSku.getId(), userId);
        data.add(productSku);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
    }

    @Override
    public void removeBySpuId(String corpid, Long spuId, String userId) {
        operationLog.add("sku:" + spuId);
        data.removeIf(item -> corpid.equals(item.getCorpid()) && spuId.equals(item.getSpuId()));
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
        Object id = condition.get("id");
        Object canPurchase = condition.get("canPurchase");
        Object enableStatus = condition.get("enableStatus");
        String keyword = condition.get("keyword") == null ? null : String.valueOf(condition.get("keyword")).trim();
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (spuId == null || spuId.equals(item.getSpuId()))
                && (id == null || id.equals(item.getId()))
                && (canPurchase == null || canPurchase.equals(item.getCanPurchase()))
                && (enableStatus == null || enableStatus.equals(item.getEnableStatus()))
                && matchesKeyword(item, keyword)
        ).toList();
    }

    private boolean matchesKeyword(ProductSku item, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        return contains(item.getSkuCode(), keyword)
            || contains(item.getSkuName(), keyword)
            || contains(item.getMnemonicCode(), keyword)
            || contains(item.getMainBarcode(), keyword);
    }

    private boolean contains(String source, String keyword) {
        return source != null && source.contains(keyword);
    }

    @Override
    public long count(Map<String, Object> condition) {
        return findByCondition(condition).size();
    }
}
