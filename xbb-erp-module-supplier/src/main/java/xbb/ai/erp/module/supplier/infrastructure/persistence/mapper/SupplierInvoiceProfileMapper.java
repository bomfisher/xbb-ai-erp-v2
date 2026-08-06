package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierInvoiceProfilePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierInvoiceProfileMapper extends BaseMapper<SupplierInvoiceProfilePO> {
    int insertBatch(@Param("list") List<SupplierInvoiceProfilePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SupplierInvoiceProfilePO po);

    SupplierInvoiceProfilePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SupplierInvoiceProfilePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
