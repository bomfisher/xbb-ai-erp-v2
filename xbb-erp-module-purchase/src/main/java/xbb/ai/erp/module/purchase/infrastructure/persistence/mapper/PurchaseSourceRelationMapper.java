package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseSourceRelationPO;

import java.util.List;
import java.util.Map;

public interface PurchaseSourceRelationMapper extends BaseMapper<PurchaseSourceRelationPO> {
    int insertBatch(@Param("list") List<PurchaseSourceRelationPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseSourceRelationPO po);

    PurchaseSourceRelationPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseSourceRelationPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
