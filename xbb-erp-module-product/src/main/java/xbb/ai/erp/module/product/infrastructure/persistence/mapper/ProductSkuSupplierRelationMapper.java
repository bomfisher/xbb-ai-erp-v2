package xbb.ai.erp.module.product.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationHistoryPO;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuSupplierRelationPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductSkuSupplierRelationMapper {

    int insert(ProductSkuSupplierRelationPO po);

    int update(ProductSkuSupplierRelationPO po);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int updateDefaultFlag(@Param("corpid") String corpid, @Param("id") Long id, @Param("defaultFlag") Integer defaultFlag, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int clearDefaultBySkuId(@Param("corpid") String corpid, @Param("skuId") Long skuId, @Param("excludeId") Long excludeId, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int updateEnableStatus(@Param("corpid") String corpid, @Param("id") Long id, @Param("enableStatus") Integer enableStatus, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    ProductSkuSupplierRelationPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<Map<String, Object>> findListByCondition(Map<String, Object> conditionMap);

    long countListByCondition(Map<String, Object> conditionMap);

    List<Map<String, Object>> findSkuOptionsBySpu(@Param("corpid") String corpid, @Param("spuId") Long spuId);

    Map<String, Object> findSkuOptionById(@Param("corpid") String corpid, @Param("skuId") Long skuId);

    List<Map<String, Object>> findSupplierOptions(@Param("corpid") String corpid, @Param("supplierId") Long supplierId, @Param("limit") Integer limit);

    int insertHistory(ProductSkuSupplierRelationHistoryPO po);

    List<ProductSkuSupplierRelationHistoryPO> findHistoryByRelationId(@Param("corpid") String corpid, @Param("relationId") Long relationId);
}
