package xbb.ai.erp.module.product.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductUnitPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductUnitMapper {

    int insert(ProductUnitPO po);

    int insertBatch(@Param("list") List<ProductUnitPO> list);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int update(ProductUnitPO po);

    ProductUnitPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ProductUnitPO> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
