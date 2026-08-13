package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSpuPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductSpuMapper extends BaseMapper<ProductSpuPO> {
    int insertBatch(@Param("list") List<ProductSpuPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(ProductSpuPO po);

    ProductSpuPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ProductSpuPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
