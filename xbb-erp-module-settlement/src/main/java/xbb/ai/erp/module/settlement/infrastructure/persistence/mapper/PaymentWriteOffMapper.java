package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentWriteOffPO;

@Mapper
public interface PaymentWriteOffMapper {
    int insert(PaymentWriteOffPO po);

    PaymentWriteOffPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PaymentWriteOffPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);

    int reverse(@Param("corpid") String corpid, @Param("id") Long id, @Param("reversedTime") Long reversedTime,
                @Param("userId") String userId);
}
