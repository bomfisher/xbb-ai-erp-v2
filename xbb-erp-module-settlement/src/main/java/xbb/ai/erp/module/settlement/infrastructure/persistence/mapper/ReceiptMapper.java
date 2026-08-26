package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReceiptMapper {
    int insert(ReceiptPO po);

    int insertBatch(@Param("list") List<ReceiptPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(ReceiptPO po);

    ReceiptPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<ReceiptPO> findByIds(@Param("corpid") String corpid, @Param("ids") java.util.Collection<Long> ids);

    List<ReceiptPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
