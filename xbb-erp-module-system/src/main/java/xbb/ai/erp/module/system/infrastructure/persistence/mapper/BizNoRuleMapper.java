package xbb.ai.erp.module.system.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.system.infrastructure.persistence.po.BizNoRulePO;

import java.util.List;

@Mapper
public interface BizNoRuleMapper {

    BizNoRulePO findByKey(@Param("corpid") String corpid, @Param("businessCode") String businessCode);

    BizNoRulePO findOwnByKey(@Param("corpid") String corpid, @Param("businessCode") String businessCode);

    List<BizNoRulePO> findAvailableByCorpid(@Param("corpid") String corpid);

    int insert(BizNoRulePO po);

    int update(BizNoRulePO po);
}
