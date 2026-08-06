package xbb.ai.erp.module.supplier.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierBankAccountPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierBankAccountMapper extends BaseMapper<SupplierBankAccountPO> {
    int insertBatch(@Param("list") List<SupplierBankAccountPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SupplierBankAccountPO po);

    SupplierBankAccountPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SupplierBankAccountPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
