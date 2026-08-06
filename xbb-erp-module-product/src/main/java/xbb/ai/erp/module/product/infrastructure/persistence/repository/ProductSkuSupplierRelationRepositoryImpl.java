package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationListItem;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;
import xbb.ai.erp.module.product.domain.repository.ProductSkuSupplierRelationRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.convertor.ProductSkuSupplierRelationConvertor;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductSkuSupplierRelationMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationHistoryPO;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationPO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductSkuSupplierRelationRepositoryImpl implements ProductSkuSupplierRelationRepository {

    private final ProductSkuSupplierRelationMapper relationMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductSkuSupplierRelation relation, String userId) {
        ProductSkuSupplierRelationPO po = ProductSkuSupplierRelationConvertor.toPO(relation);
        long now = System.currentTimeMillis();
        if (po.getId() == null) {
            po.setId(idGenerator.nextId());
        }
        po.setDel(po.getDel() == null ? 0 : po.getDel());
        po.setVersion(po.getVersion() == null ? 0 : po.getVersion());
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        relationMapper.insert(po);
        relation.setId(po.getId());
        return po.getId();
    }

    @Override
    public void update(ProductSkuSupplierRelation relation, String userId) {
        ProductSkuSupplierRelationPO po = ProductSkuSupplierRelationConvertor.toPO(relation);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        relationMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        relationMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public void updateDefaultFlag(String corpid, Long id, Integer defaultFlag, String userId) {
        relationMapper.updateDefaultFlag(corpid, id, defaultFlag, userId, System.currentTimeMillis());
    }

    @Override
    public void clearDefaultBySkuId(String corpid, Long skuId, Long excludeId, String userId) {
        relationMapper.clearDefaultBySkuId(corpid, skuId, excludeId, userId, System.currentTimeMillis());
    }

    @Override
    public void updateEnableStatus(String corpid, Long id, Integer enableStatus, String userId) {
        relationMapper.updateEnableStatus(corpid, id, enableStatus, userId, System.currentTimeMillis());
    }

    @Override
    public ProductSkuSupplierRelation findById(String corpid, Long id) {
        return ProductSkuSupplierRelationConvertor.toDomain(relationMapper.findById(corpid, id));
    }

    @Override
    public List<ProductSkuSupplierRelationListItem> findListByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> prepared = QueryConditionMapHelper.prepare(conditionMap);
        return relationMapper.findListByCondition(prepared).stream().map(this::toListItem).toList();
    }

    @Override
    public long countListByCondition(Map<String, Object> conditionMap) {
        return relationMapper.countListByCondition(QueryConditionMapHelper.prepare(conditionMap));
    }

    @Override
    public List<ProductSkuSupplierRelationSkuOption> findSkuOptionsBySpu(String corpid, Long spuId) {
        return relationMapper.findSkuOptionsBySpu(corpid, spuId).stream().map(this::toSkuOption).toList();
    }

    @Override
    public ProductSkuSupplierRelationSkuOption findSkuOptionById(String corpid, Long skuId) {
        return toSkuOption(relationMapper.findSkuOptionById(corpid, skuId));
    }

    @Override
    public List<ProductSkuSupplierRelationSupplierOption> findSupplierOptions(String corpid, Long supplierId, Integer limit) {
        return relationMapper.findSupplierOptions(corpid, supplierId, limit).stream().map(this::toSupplierOption).toList();
    }

    @Override
    public void insertHistory(ProductSkuSupplierRelationHistory history) {
        ProductSkuSupplierRelationHistoryPO po = ProductSkuSupplierRelationConvertor.toPO(history);
        long now = System.currentTimeMillis();
        if (po.getId() == null) {
            po.setId(idGenerator.nextId());
        }
        po.setDel(po.getDel() == null ? 0 : po.getDel());
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(history.getOperatorId());
        po.setModifyId(history.getOperatorId());
        relationMapper.insertHistory(po);
    }

    @Override
    public List<ProductSkuSupplierRelationHistory> findHistoryByRelationId(String corpid, Long relationId) {
        return relationMapper.findHistoryByRelationId(corpid, relationId).stream().map(ProductSkuSupplierRelationConvertor::toDomain).toList();
    }

    private ProductSkuSupplierRelationListItem toListItem(Map<String, Object> row) {
        ProductSkuSupplierRelationListItem item = new ProductSkuSupplierRelationListItem();
        item.setId(toLong(row.get("id")));
        item.setSkuId(toLong(row.get("skuId")));
        item.setSkuCode((String) row.get("skuCode"));
        item.setSkuName((String) row.get("skuName"));
        item.setSpecSnapshot((String) row.get("specSnapshot"));
        item.setSupplierId(toLong(row.get("supplierId")));
        item.setSupplierCode((String) row.get("supplierCode"));
        item.setSupplierName((String) row.get("supplierName"));
        item.setPurchasePrice(toBigDecimal(row.get("purchasePrice")));
        item.setDeliveryCycleDay(toInteger(row.get("deliveryCycleDay")));
        item.setMinOrderQty(toBigDecimal(row.get("minOrderQty")));
        item.setSupplierSkuCode((String) row.get("supplierSkuCode"));
        item.setDefaultFlag(toInteger(row.get("defaultFlag")));
        item.setEnableStatus(toInteger(row.get("enableStatus")));
        item.setRemark((String) row.get("remark"));
        item.setUpdateTime(toLong(row.get("updateTime")));
        return item;
    }

    private ProductSkuSupplierRelationSkuOption toSkuOption(Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        ProductSkuSupplierRelationSkuOption option = new ProductSkuSupplierRelationSkuOption();
        option.setSkuId(toLong(row.get("skuId")));
        option.setSpuId(toLong(row.get("spuId")));
        option.setSkuCode((String) row.get("skuCode"));
        option.setSkuName((String) row.get("skuName"));
        option.setSpecSnapshot((String) row.get("specSnapshot"));
        return option;
    }

    private ProductSkuSupplierRelationSupplierOption toSupplierOption(Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        ProductSkuSupplierRelationSupplierOption option = new ProductSkuSupplierRelationSupplierOption();
        option.setSupplierId(toLong(row.get("supplierId")));
        option.setSupplierCode((String) row.get("supplierCode"));
        option.setSupplierName((String) row.get("supplierName"));
        return option;
    }

    private Long toLong(Object value) {
        if (value instanceof Long longValue) {
            return longValue;
        }
        if (value instanceof Integer integerValue) {
            return integerValue.longValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Long.parseLong(stringValue);
        }
        return null;
    }

    private Integer toInteger(Object value) {
        if (value instanceof Integer integerValue) {
            return integerValue;
        }
        if (value instanceof Long longValue) {
            return longValue.intValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Integer.parseInt(stringValue);
        }
        return null;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return new BigDecimal(stringValue);
        }
        return null;
    }
}
