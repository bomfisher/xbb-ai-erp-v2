package xbb.ai.erp.module.inventory.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockBalancePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockBalanceMapper extends BaseMapper<StockBalancePO> {
    int insertBatch(@Param("list") List<StockBalancePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(StockBalancePO po);

    StockBalancePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    StockBalancePO findByWarehouseAndSku(@Param("corpid") String corpid, @Param("warehouseId") Long warehouseId,
                                         @Param("skuId") Long skuId);

    List<StockBalancePO> findByWarehouseAndSkuPairs(@Param("corpid") String corpid, @Param("stockKeys") List<StockBalancePO> stockKeys);

    int updateWithVersion(@Param("po") StockBalancePO po, @Param("expectedVersion") Integer expectedVersion);

    List<StockBalancePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
