package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerAddressPO;

import java.util.List;
import java.util.Map;

public interface CustomerAddressMapper extends BaseMapper<CustomerAddressPO> {
    int insertBatch(@Param("list") List<CustomerAddressPO> customerAddressPOList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(CustomerAddressPO customerAddressPO);

    CustomerAddressPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<CustomerAddressPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);
}
