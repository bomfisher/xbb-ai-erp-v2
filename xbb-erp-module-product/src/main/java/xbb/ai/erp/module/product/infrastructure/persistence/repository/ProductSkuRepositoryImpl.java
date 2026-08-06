package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductSkuMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductSkuRepositoryImpl implements ProductSkuRepository {

    private final ProductSkuMapper productSkuMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductSku productSku, String userId) {
        long now = System.currentTimeMillis();
        ProductSkuPO po = toPO(productSku);
        po.setId(idGenerator.nextId());
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        productSkuMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(ProductSku productSku, String userId) {
        ProductSkuPO po = toPO(productSku);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        productSkuMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        productSkuMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public void removeBySpuId(String corpid, Long spuId, String userId) {
        productSkuMapper.removeBySpuId(corpid, spuId, userId, System.currentTimeMillis());
    }

    @Override
    public ProductSku findById(String corpid, Long id) {
        return toDomain(productSkuMapper.findById(corpid, id));
    }

    @Override
    public List<ProductSku> findBySpuId(String corpid, Long spuId) {
        return productSkuMapper.findBySpuId(corpid, spuId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ProductSku> findByCondition(Map<String, Object> condition) {
        return productSkuMapper.findByCondition(condition).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count(Map<String, Object> condition) {
        return productSkuMapper.count(condition);
    }

    private ProductSkuPO toPO(ProductSku productSku) {
        if (productSku == null) {
            return null;
        }
        ProductSkuPO po = new ProductSkuPO();
        po.setId(productSku.getId());
        po.setCorpid(productSku.getCorpid());
        po.setSpuId(productSku.getSpuId());
        po.setSkuCode(productSku.getSkuCode());
        po.setSkuName(productSku.getSkuName());
        po.setMnemonicCode(productSku.getMnemonicCode());
        po.setMainBarcode(productSku.getMainBarcode());
        po.setSpecSignature(productSku.getSpecSignature());
        po.setSpecSnapshot(productSku.getSpecSnapshot());
        po.setCanPurchase(productSku.getCanPurchase());
        po.setCanSale(productSku.getCanSale());
        po.setCanInventory(productSku.getCanInventory());
        po.setCanProduce(productSku.getCanProduce());
        po.setEnableStatus(productSku.getEnableStatus());
        po.setListingStatus(productSku.getListingStatus());
        return po;
    }

    private ProductSku toDomain(ProductSkuPO po) {
        if (po == null) {
            return null;
        }
        ProductSku productSku = new ProductSku();
        productSku.setId(po.getId());
        productSku.setCorpid(po.getCorpid());
        productSku.setSpuId(po.getSpuId());
        productSku.setSkuCode(po.getSkuCode());
        productSku.setSkuName(po.getSkuName());
        productSku.setMnemonicCode(po.getMnemonicCode());
        productSku.setMainBarcode(po.getMainBarcode());
        productSku.setSpecSignature(po.getSpecSignature());
        productSku.setSpecSnapshot(po.getSpecSnapshot());
        productSku.setCanPurchase(po.getCanPurchase());
        productSku.setCanSale(po.getCanSale());
        productSku.setCanInventory(po.getCanInventory());
        productSku.setCanProduce(po.getCanProduce());
        productSku.setEnableStatus(po.getEnableStatus());
        productSku.setListingStatus(po.getListingStatus());
        return productSku;
    }
}
