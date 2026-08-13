package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSpuPO;

public final class ProductSpuConvertor {

    private ProductSpuConvertor() {
    }

    public static ProductSpuPO toPO(ProductSpu productSpu) {
        if (productSpu == null) {
            return null;
        }
        ProductSpuPO po = new ProductSpuPO();
        po.setId(productSpu.getId());
        po.setCorpid(productSpu.getCorpid());
        po.setSpuCode(productSpu.getSpuCode());
        po.setSpuName(productSpu.getSpuName());
        po.setCategoryName(productSpu.getCategoryName());
        po.setEnabled(productSpu.getEnabled());
        po.setRemark(productSpu.getRemark());
        po.setCreatorId(productSpu.getCreatorId());
        po.setModifyId(productSpu.getModifyId());
        return po;
    }

    public static ProductSpu toDomain(ProductSpuPO po) {
        if (po == null) {
            return null;
        }
        ProductSpu productSpu = new ProductSpu();
        productSpu.setId(po.getId());
        productSpu.setCorpid(po.getCorpid());
        productSpu.setSpuCode(po.getSpuCode());
        productSpu.setSpuName(po.getSpuName());
        productSpu.setCategoryName(po.getCategoryName());
        productSpu.setEnabled(po.getEnabled());
        productSpu.setRemark(po.getRemark());
        productSpu.setCreatorId(po.getCreatorId());
        productSpu.setModifyId(po.getModifyId());
        return productSpu;
    }
}
