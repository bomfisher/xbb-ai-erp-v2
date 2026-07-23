package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorPO;

import java.util.List;
import java.util.Map;

public interface VendorMapper extends BaseMapper<VendorPO> {
    int insertBatch(@Param("list") List<VendorPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(VendorPO po);

    VendorPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<VendorPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
