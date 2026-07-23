package xbb.ai.erp.module.product.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductBrandPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductBrandMapper {

    int insert(ProductBrandPO po);

    int insertBatch(@Param("list") List<ProductBrandPO> list);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int update(ProductBrandPO po);

    ProductBrandPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ProductBrandPO> findByCondition(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
