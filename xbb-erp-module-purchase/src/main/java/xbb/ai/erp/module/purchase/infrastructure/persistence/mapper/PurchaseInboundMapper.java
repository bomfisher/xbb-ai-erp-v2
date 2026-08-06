package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInboundPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseInboundMapper extends BaseMapper<PurchaseInboundPO> {
    int insertBatch(@Param("list") List<PurchaseInboundPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseInboundPO po);

    PurchaseInboundPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseInboundPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
