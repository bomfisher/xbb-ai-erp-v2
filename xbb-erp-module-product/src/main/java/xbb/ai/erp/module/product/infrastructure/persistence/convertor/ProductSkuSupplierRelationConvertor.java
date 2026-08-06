package xbb.ai.erp.module.product.infrastructure.persistence.convertor;

import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationHistoryPO;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationPO;

public final class ProductSkuSupplierRelationConvertor {

    private ProductSkuSupplierRelationConvertor() {
    }

    public static ProductSkuSupplierRelationPO toPO(ProductSkuSupplierRelation relation) {
        if (relation == null) {
            return null;
        }
        ProductSkuSupplierRelationPO po = new ProductSkuSupplierRelationPO();
        po.setId(relation.getId());
        po.setCorpid(relation.getCorpid());
        po.setSkuId(relation.getSkuId());
        po.setSupplierId(relation.getSupplierId());
        po.setPurchasePrice(relation.getPurchasePrice());
        po.setDeliveryCycleDay(relation.getDeliveryCycleDay());
        po.setMinOrderQty(relation.getMinOrderQty());
        po.setSupplierSkuCode(relation.getSupplierSkuCode());
        po.setDefaultFlag(relation.getDefaultFlag());
        po.setEnableStatus(relation.getEnableStatus());
        po.setRemark(relation.getRemark());
        po.setVersion(relation.getVersion());
        po.setDel(relation.getDel());
        po.setAddTime(relation.getAddTime());
        po.setUpdateTime(relation.getUpdateTime());
        po.setCreatorId(relation.getCreatorId());
        po.setModifyId(relation.getModifyId());
        return po;
    }

    public static ProductSkuSupplierRelation toDomain(ProductSkuSupplierRelationPO po) {
        if (po == null) {
            return null;
        }
        ProductSkuSupplierRelation relation = new ProductSkuSupplierRelation();
        relation.setId(po.getId());
        relation.setCorpid(po.getCorpid());
        relation.setSkuId(po.getSkuId());
        relation.setSupplierId(po.getSupplierId());
        relation.setPurchasePrice(po.getPurchasePrice());
        relation.setDeliveryCycleDay(po.getDeliveryCycleDay());
        relation.setMinOrderQty(po.getMinOrderQty());
        relation.setSupplierSkuCode(po.getSupplierSkuCode());
        relation.setDefaultFlag(po.getDefaultFlag());
        relation.setEnableStatus(po.getEnableStatus());
        relation.setRemark(po.getRemark());
        relation.setVersion(po.getVersion());
        relation.setDel(po.getDel());
        relation.setAddTime(po.getAddTime());
        relation.setUpdateTime(po.getUpdateTime());
        relation.setCreatorId(po.getCreatorId());
        relation.setModifyId(po.getModifyId());
        return relation;
    }

    public static ProductSkuSupplierRelationHistoryPO toPO(ProductSkuSupplierRelationHistory history) {
        if (history == null) {
            return null;
        }
        ProductSkuSupplierRelationHistoryPO po = new ProductSkuSupplierRelationHistoryPO();
        po.setId(history.getId());
        po.setCorpid(history.getCorpid());
        po.setRelationId(history.getRelationId());
        po.setOperateType(history.getOperateType());
        po.setOperatorId(history.getOperatorId());
        po.setChangeSnapshot(history.getChangeSnapshot());
        po.setRemark(history.getRemark());
        po.setDel(history.getDel());
        po.setAddTime(history.getAddTime());
        po.setUpdateTime(history.getUpdateTime());
        po.setCreatorId(history.getCreatorId());
        po.setModifyId(history.getModifyId());
        return po;
    }

    public static ProductSkuSupplierRelationHistory toDomain(ProductSkuSupplierRelationHistoryPO po) {
        if (po == null) {
            return null;
        }
        ProductSkuSupplierRelationHistory history = new ProductSkuSupplierRelationHistory();
        history.setId(po.getId());
        history.setCorpid(po.getCorpid());
        history.setRelationId(po.getRelationId());
        history.setOperateType(po.getOperateType());
        history.setOperatorId(po.getOperatorId());
        history.setChangeSnapshot(po.getChangeSnapshot());
        history.setRemark(po.getRemark());
        history.setDel(po.getDel());
        history.setAddTime(po.getAddTime());
        history.setUpdateTime(po.getUpdateTime());
        history.setCreatorId(po.getCreatorId());
        history.setModifyId(po.getModifyId());
        return history;
    }
}
