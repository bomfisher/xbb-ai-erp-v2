package xbb.ai.erp.module.product.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.product.admin.vo.ProductSpuSkuListVO;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSkuPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductSkuMapper {

    int insert(ProductSkuPO po);

    int insertBatch(@Param("list") List<ProductSkuPO> list);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int removeBySpuId(@Param("corpid") String corpid, @Param("spuId") Long spuId, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int update(ProductSkuPO po);

    ProductSkuPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    ProductSkuPO findBySpuId(@Param("corpid") String corpid, @Param("spuId") Long spuId);

    List<ProductSkuPO> findByCondition(Map<String, Object> condition);

    List<ProductSpuSkuListVO> findSpuSkuList(Map<String, Object> condition);

    long count(Map<String, Object> condition);
}
