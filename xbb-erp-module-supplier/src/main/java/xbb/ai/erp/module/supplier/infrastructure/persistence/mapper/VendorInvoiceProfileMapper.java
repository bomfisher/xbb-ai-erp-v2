package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorInvoiceProfilePO;

import java.util.List;
import java.util.Map;

public interface VendorInvoiceProfileMapper extends BaseMapper<VendorInvoiceProfilePO> {
    int insertBatch(@Param("list") List<VendorInvoiceProfilePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(VendorInvoiceProfilePO po);

    VendorInvoiceProfilePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<VendorInvoiceProfilePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
