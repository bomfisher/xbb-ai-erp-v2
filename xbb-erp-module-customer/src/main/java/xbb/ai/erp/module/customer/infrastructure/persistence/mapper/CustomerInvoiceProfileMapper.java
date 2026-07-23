package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerInvoiceProfilePO;

import java.util.List;
import java.util.Map;

public interface CustomerInvoiceProfileMapper extends BaseMapper<CustomerInvoiceProfilePO> {
    int insertBatch(@Param("list") List<CustomerInvoiceProfilePO> customerInvoiceProfilePOList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(CustomerInvoiceProfilePO customerInvoiceProfilePO);

    CustomerInvoiceProfilePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<CustomerInvoiceProfilePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);
}
