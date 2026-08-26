package xbb.ai.erp.module.approval.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalFlowNodePO;

import java.util.List;

@Mapper
public interface ApprovalFlowNodeMapper {
    List<ApprovalFlowNodePO> findByDefinitionId(@Param("flowDefinitionId") Long flowDefinitionId);
    int insert(ApprovalFlowNodePO po);
    int removeByDefinitionId(@Param("flowDefinitionId") Long flowDefinitionId, @Param("updateTime") long updateTime);
}
