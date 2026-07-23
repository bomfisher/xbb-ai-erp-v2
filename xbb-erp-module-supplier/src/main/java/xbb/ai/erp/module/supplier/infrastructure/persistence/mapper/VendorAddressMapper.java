package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorAddressPO;

import java.util.List;
import java.util.Map;

public interface VendorAddressMapper extends BaseMapper<VendorAddressPO> {
    int insertBatch(@Param("list") List<VendorAddressPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(VendorAddressPO po);

    VendorAddressPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<VendorAddressPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
