package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundItemPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesOutboundItemMapper {
    int insert(SalesOutboundItemPO po);

    int insertBatch(@Param("list") List<SalesOutboundItemPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesOutboundItemPO po);

    SalesOutboundItemPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesOutboundItemPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
