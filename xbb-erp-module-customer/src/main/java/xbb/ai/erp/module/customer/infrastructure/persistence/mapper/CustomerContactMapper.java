package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerContactPO;

import java.util.List;
import java.util.Map;

public interface CustomerContactMapper extends BaseMapper<CustomerContactPO> {
    int insertBatch(@Param("list") List<CustomerContactPO> customerContactPOList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(CustomerContactPO customerContactPO);

    CustomerContactPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<CustomerContactPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);
}
