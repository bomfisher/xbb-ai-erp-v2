package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerContactPO;

@Mapper
public interface CustomerContactMapper extends BaseMapper<CustomerContactPO> {
    List<CustomerContactPO> findByCustomerId(@Param("corpid") String corpid, @Param("customerId") Long customerId);

    int removeMissing(@Param("corpid") String corpid, @Param("customerId") Long customerId, @Param("ids") List<Long> ids);

    int removeAll(@Param("corpid") String corpid, @Param("customerId") Long customerId);

    int update(CustomerContactPO contact);
}
