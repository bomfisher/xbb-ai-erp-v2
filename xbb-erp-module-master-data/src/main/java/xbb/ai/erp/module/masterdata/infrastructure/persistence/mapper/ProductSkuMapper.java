package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSkuPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuPO> {
    List<ProductSkuPO> findByIds(@Param("corpid") String corpid, @Param("ids") java.util.Collection<Long> ids);
    int insertBatch(@Param("list") List<ProductSkuPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(ProductSkuPO po);

    ProductSkuPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ProductSkuPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
