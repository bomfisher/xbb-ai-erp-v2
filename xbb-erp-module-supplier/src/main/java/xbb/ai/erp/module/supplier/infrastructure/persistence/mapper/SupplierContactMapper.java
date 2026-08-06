package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierContactPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierContactMapper extends BaseMapper<SupplierContactPO> {
    int insertBatch(@Param("list") List<SupplierContactPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SupplierContactPO po);

    SupplierContactPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SupplierContactPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
