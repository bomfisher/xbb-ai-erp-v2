package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierAddressPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierAddressMapper extends BaseMapper<SupplierAddressPO> {
    int insertBatch(@Param("list") List<SupplierAddressPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SupplierAddressPO po);

    SupplierAddressPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SupplierAddressPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
