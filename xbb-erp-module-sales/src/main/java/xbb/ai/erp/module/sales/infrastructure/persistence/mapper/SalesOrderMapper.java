package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesOrderMapper {
    int insert(SalesOrderPO po);

    int insertBatch(@Param("list") List<SalesOrderPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesOrderPO po);

    SalesOrderPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesOrderPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
