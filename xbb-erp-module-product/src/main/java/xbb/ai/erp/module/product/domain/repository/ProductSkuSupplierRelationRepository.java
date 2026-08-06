package xbb.ai.erp.module.product.domain.repository;

import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationListItem;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;

import java.util.List;
import java.util.Map;

public interface ProductSkuSupplierRelationRepository {

    Long save(ProductSkuSupplierRelation relation, String userId);

    void update(ProductSkuSupplierRelation relation, String userId);

    void removeById(String corpid, Long id, String userId);

    void updateDefaultFlag(String corpid, Long id, Integer defaultFlag, String userId);

    void clearDefaultBySkuId(String corpid, Long skuId, Long excludeId, String userId);

    void updateEnableStatus(String corpid, Long id, Integer enableStatus, String userId);

    ProductSkuSupplierRelation findById(String corpid, Long id);

    List<ProductSkuSupplierRelationListItem> findListByCondition(Map<String, Object> conditionMap);

    long countListByCondition(Map<String, Object> conditionMap);

    List<ProductSkuSupplierRelationSkuOption> findSkuOptionsBySpu(String corpid, Long spuId);

    ProductSkuSupplierRelationSkuOption findSkuOptionById(String corpid, Long skuId);

    List<ProductSkuSupplierRelationSupplierOption> findSupplierOptions(String corpid, Long supplierId, Integer limit);

    void insertHistory(ProductSkuSupplierRelationHistory history);

    List<ProductSkuSupplierRelationHistory> findHistoryByRelationId(String corpid, Long relationId);
}
