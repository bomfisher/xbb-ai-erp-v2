package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptWriteOffPO;

@Mapper
public interface ReceiptWriteOffMapper {
    int insert(ReceiptWriteOffPO po);

    ReceiptWriteOffPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ReceiptWriteOffPO> findActiveByReceiptId(@Param("corpid") String corpid, @Param("receiptId") Long receiptId);

    int update(ReceiptWriteOffPO po);

    int reverse(@Param("corpid") String corpid, @Param("id") Long id, @Param("reversedTime") Long reversedTime,
                @Param("userId") String userId);

    List<ReceiptWriteOffPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
