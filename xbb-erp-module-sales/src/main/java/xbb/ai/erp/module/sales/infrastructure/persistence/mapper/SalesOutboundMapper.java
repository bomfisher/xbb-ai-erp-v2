package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundPO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface SalesOutboundMapper {
    int insert(SalesOutboundPO po);

    int insertBatch(@Param("list") List<SalesOutboundPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesOutboundPO po);

    SalesOutboundPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesOutboundPO> findByIds(@Param("corpid") String corpid, @Param("ids") Collection<Long> ids);

    List<SalesOutboundPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
