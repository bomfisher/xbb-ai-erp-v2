package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;

import java.util.List;
import java.util.Map;

public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItemPO> {
    int insertBatch(@Param("list") List<PurchaseOrderItemPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseOrderItemPO po);

    PurchaseOrderItemPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseOrderItemPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
