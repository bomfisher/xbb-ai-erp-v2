package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSkuPO;

public final class ProductSkuConvertor {

    private ProductSkuConvertor() {
    }

    public static ProductSkuPO toPO(ProductSku productSku) {
        if (productSku == null) {
            return null;
        }
        ProductSkuPO po = new ProductSkuPO();
        po.setId(productSku.getId());
        po.setCorpid(productSku.getCorpid());
        po.setSpuId(productSku.getSpuId());
        po.setSkuCode(productSku.getSkuCode());
        po.setSkuName(productSku.getSkuName());
        po.setSpecification(productSku.getSpecification());
        po.setUnitName(productSku.getUnitName());
        po.setSalePrice(productSku.getSalePrice());
        po.setPurchasePrice(productSku.getPurchasePrice());
        po.setEnabled(productSku.getEnabled());
        po.setRemark(productSku.getRemark());
        po.setCreatorId(productSku.getCreatorId());
        po.setModifyId(productSku.getModifyId());
        return po;
    }

    public static ProductSku toDomain(ProductSkuPO po) {
        if (po == null) {
            return null;
        }
        ProductSku productSku = new ProductSku();
        productSku.setId(po.getId());
        productSku.setCorpid(po.getCorpid());
        productSku.setSpuId(po.getSpuId());
        productSku.setSkuCode(po.getSkuCode());
        productSku.setSkuName(po.getSkuName());
        productSku.setSpecification(po.getSpecification());
        productSku.setUnitName(po.getUnitName());
        productSku.setSalePrice(po.getSalePrice());
        productSku.setPurchasePrice(po.getPurchasePrice());
        productSku.setEnabled(po.getEnabled());
        productSku.setRemark(po.getRemark());
        productSku.setCreatorId(po.getCreatorId());
        productSku.setModifyId(po.getModifyId());
        return productSku;
    }
}
