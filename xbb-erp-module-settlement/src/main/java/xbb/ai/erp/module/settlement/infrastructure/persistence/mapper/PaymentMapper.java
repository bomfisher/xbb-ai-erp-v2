package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentMapper {
    int insert(PaymentPO po);

    int insertBatch(@Param("list") List<PaymentPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PaymentPO po);

    PaymentPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PaymentPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
