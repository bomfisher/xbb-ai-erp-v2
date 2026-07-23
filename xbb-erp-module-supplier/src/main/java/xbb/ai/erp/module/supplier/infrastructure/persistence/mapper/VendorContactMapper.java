package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorContactPO;

import java.util.List;
import java.util.Map;

public interface VendorContactMapper extends BaseMapper<VendorContactPO> {
    int insertBatch(@Param("list") List<VendorContactPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(VendorContactPO po);

    VendorContactPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<VendorContactPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
