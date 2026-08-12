package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerMapper extends BaseMapper<CustomerPO> {
    int insertBatch(@Param("list") List<CustomerPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(CustomerPO po);

    int updateDefaultContactId(@Param("corpid") String corpid, @Param("id") Long id, @Param("defaultContactId") Long defaultContactId);

    CustomerPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<CustomerPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
