package xbb.ai.erp.module.demo.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoPO;

import java.util.List;
import java.util.Map;
import java.util.Collection;

@Mapper
public interface DemoMapper extends BaseMapper<DemoPO> {
    int insertBatch(@Param("list") List<DemoPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(DemoPO po);

    DemoPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<DemoPO> findByIds(@Param("corpid") String corpid, @Param("ids") Collection<Long> ids);

    List<DemoPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
