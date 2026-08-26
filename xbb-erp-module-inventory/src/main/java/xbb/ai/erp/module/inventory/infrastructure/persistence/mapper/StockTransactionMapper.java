package xbb.ai.erp.module.inventory.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockTransactionPO;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.domain.pojo.StockTransactionQueryPojo;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockTransactionMapper extends BaseMapper<StockTransactionPO> {
    List<StockTransactionQueryPojo> queryList(@Param("query") StockTransactionQueryDTO query, @Param("offset") int offset, @Param("pageSize") int pageSize);
    Long queryCount(@Param("query") StockTransactionQueryDTO query);
    int insertBatch(@Param("list") List<StockTransactionPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(StockTransactionPO po);

    StockTransactionPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<StockTransactionPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
