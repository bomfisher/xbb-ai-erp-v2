package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequestPO> {
    int insertBatch(@Param("list") List<PurchaseRequestPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseRequestPO po);

    PurchaseRequestPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseRequestPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
