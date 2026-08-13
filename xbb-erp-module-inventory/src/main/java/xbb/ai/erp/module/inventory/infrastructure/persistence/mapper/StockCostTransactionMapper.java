package xbb.ai.erp.module.inventory.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockCostTransactionPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockCostTransactionMapper extends BaseMapper<StockCostTransactionPO> {
    int insertBatch(@Param("list") List<StockCostTransactionPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(StockCostTransactionPO po);

    StockCostTransactionPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    StockCostTransactionPO findByIdempotencyKey(@Param("corpid") String corpid, @Param("idempotencyKey") String idempotencyKey);

    List<StockCostTransactionPO> findByIdempotencyKeys(@Param("corpid") String corpid, @Param("idempotencyKeys") List<String> idempotencyKeys);

    List<StockCostTransactionPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
