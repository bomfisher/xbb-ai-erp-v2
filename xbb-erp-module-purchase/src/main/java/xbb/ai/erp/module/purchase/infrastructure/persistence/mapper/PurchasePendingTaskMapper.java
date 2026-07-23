package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchasePendingTaskPO;

import java.util.List;
import java.util.Map;

public interface PurchasePendingTaskMapper extends BaseMapper<PurchasePendingTaskPO> {
    int insertBatch(@Param("list") List<PurchasePendingTaskPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchasePendingTaskPO po);

    PurchasePendingTaskPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchasePendingTaskPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
