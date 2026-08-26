package xbb.ai.erp.module.approval.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalFlowDefinitionPO;

import java.util.List;

@Mapper
public interface ApprovalFlowDefinitionMapper {
    ApprovalFlowDefinitionPO findById(@Param("corpid") String corpid, @Param("id") Long id);
    List<ApprovalFlowDefinitionPO> findByCondition(@Param("corpid") String corpid, @Param("businessCode") String businessCode,
        @Param("approvalScene") String approvalScene);
    Integer findMaxVersion(@Param("corpid") String corpid, @Param("businessCode") String businessCode,
                           @Param("approvalScene") String approvalScene, @Param("flowCode") String flowCode);
    int insert(ApprovalFlowDefinitionPO po);
    int update(ApprovalFlowDefinitionPO po);

    int remove(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId,
               @Param("updateTime") long updateTime);
}
