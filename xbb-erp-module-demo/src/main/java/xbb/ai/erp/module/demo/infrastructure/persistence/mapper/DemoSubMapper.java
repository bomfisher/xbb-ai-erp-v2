package xbb.ai.erp.module.demo.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoSubPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface DemoSubMapper extends BaseMapper<DemoSubPO> {
    int insertBatch(@Param("list") List<DemoSubPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(DemoSubPO po);

    DemoSubPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<DemoSubPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
