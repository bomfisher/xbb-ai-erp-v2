package xbb.ai.erp.module.approval.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalNodeApproverPO;

import java.util.List;

@Mapper
public interface ApprovalNodeApproverMapper {
    List<ApprovalNodeApproverPO> findByNodeIds(@Param("nodeIds") List<Long> nodeIds);
    int insert(ApprovalNodeApproverPO po);
    int removeByNodeIds(@Param("nodeIds") List<Long> nodeIds, @Param("updateTime") long updateTime);
}
