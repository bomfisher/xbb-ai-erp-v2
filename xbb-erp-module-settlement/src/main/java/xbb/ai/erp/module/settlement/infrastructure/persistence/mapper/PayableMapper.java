package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PayablePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayableMapper {
    int insert(PayablePO po);

    int insertBatch(@Param("list") List<PayablePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PayablePO po);

    PayablePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PayablePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
