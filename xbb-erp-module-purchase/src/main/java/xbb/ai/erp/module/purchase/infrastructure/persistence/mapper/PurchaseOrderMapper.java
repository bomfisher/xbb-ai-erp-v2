package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderPO;

import java.util.List;
import java.util.Map;

public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrderPO> {
    int insertBatch(@Param("list") List<PurchaseOrderPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseOrderPO po);

    PurchaseOrderPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseOrderPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
