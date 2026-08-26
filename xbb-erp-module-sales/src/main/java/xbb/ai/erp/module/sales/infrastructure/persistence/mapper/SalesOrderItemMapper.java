package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderItemPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesOrderItemMapper {
    int insert(SalesOrderItemPO po);

    int insertBatch(@Param("list") List<SalesOrderItemPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesOrderItemPO po);

    SalesOrderItemPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    SalesOrderItemPO findByIdForUpdate(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesOrderItemPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
