package xbb.ai.erp.module.demo.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoItemPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface DemoItemMapper extends BaseMapper<DemoItemPO> {
    int insertBatch(@Param("list") List<DemoItemPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(DemoItemPO po);

    DemoItemPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<DemoItemPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
