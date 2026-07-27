package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestItemPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseRequestItemMapper extends BaseMapper<PurchaseRequestItemPO> {
    int insertBatch(@Param("list") List<PurchaseRequestItemPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseRequestItemPO po);

    PurchaseRequestItemPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseRequestItemPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
