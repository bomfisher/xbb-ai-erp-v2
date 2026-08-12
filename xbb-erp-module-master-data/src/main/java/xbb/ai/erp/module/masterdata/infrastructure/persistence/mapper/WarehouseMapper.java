package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.WarehousePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseMapper extends BaseMapper<WarehousePO> {
    int insertBatch(@Param("list") List<WarehousePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(WarehousePO po);

    WarehousePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<WarehousePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
