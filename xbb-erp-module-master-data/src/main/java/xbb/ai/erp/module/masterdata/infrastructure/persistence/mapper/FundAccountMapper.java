package xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.FundAccountPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface FundAccountMapper {
    int insert(FundAccountPO po);

    int insertBatch(@Param("list") List<FundAccountPO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(FundAccountPO po);

    FundAccountPO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<FundAccountPO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
