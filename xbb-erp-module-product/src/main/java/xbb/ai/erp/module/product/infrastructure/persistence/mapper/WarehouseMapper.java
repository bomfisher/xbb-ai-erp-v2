package xbb.ai.erp.module.product.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.WarehousePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseMapper extends BaseMapper<WarehousePO> {

    int insertBatch(@Param("list") List<WarehousePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);

    int update(WarehousePO po);

    WarehousePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<WarehousePO> findByCondition(Map<String, Object> conditionMap);

    long count(Map<String, Object> conditionMap);
}
