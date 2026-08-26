package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceivablePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReceivableMapper {
    int insert(ReceivablePO po);

    int insertBatch(@Param("list") List<ReceivablePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(ReceivablePO po);

    ReceivablePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ReceivablePO> findByIds(@Param("corpid") String corpid, @Param("ids") java.util.Collection<Long> ids);

    List<ReceivablePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
