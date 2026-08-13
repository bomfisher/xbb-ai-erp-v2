package xbb.ai.erp.module.inventory.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockReservationPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockReservationMapper extends BaseMapper<StockReservationPO> {
    int insertBatch(@Param("list") List<StockReservationPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(StockReservationPO po);

    StockReservationPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<StockReservationPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
