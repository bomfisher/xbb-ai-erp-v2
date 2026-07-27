package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerBankAccountMapper extends BaseMapper<CustomerBankAccountPO> {
    int insertBatch(@Param("list") List<CustomerBankAccountPO> customerBankAccountPOList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(CustomerBankAccountPO customerBankAccountPO);

    CustomerBankAccountPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<CustomerBankAccountPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);
}
