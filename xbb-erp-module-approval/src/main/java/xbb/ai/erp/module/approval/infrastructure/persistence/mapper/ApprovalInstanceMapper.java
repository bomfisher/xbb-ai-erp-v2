package xbb.ai.erp.module.approval.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstancePO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstanceActionLogPO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstanceTaskPO;
import java.util.List;

@Mapper
public interface ApprovalInstanceMapper {
    ApprovalInstancePO findById(@Param("corpid") String corpid, @Param("instanceId") String instanceId);
    ApprovalInstancePO findByRequestId(@Param("corpid") String corpid, @Param("requestId") String requestId);
    List<ApprovalInstanceTaskPO> findTasks(@Param("instanceId") String instanceId, @Param("nodeNo") Integer nodeNo);
    List<ApprovalInstancePO> findByApprovalCenter(@Param("corpid") String corpid, @Param("userId") String userId,
                                                   @Param("view") String view, @Param("status") String status,
                                                   @Param("businessCode") String businessCode, @Param("keyword") String keyword);
    List<ApprovalInstanceActionLogPO> findActionLogs(@Param("instanceId") String instanceId);
    int countApprovalCenterAccess(@Param("corpid") String corpid, @Param("userId") String userId,
                                  @Param("instanceId") String instanceId);
    int insert(ApprovalInstancePO po);
    int insertTask(ApprovalInstanceTaskPO po);
    int update(ApprovalInstancePO po);
    int updateTask(ApprovalInstanceTaskPO po);
    int activateTasks(@Param("instanceId") String instanceId, @Param("nodeNo") Integer nodeNo,
                      @Param("activatedAt") long activatedAt);
    int cancelTasks(@Param("instanceId") String instanceId, @Param("nodeNo") Integer nodeNo, @Param("handledAt") long handledAt);
    int appendAction(@Param("instanceId") String instanceId, @Param("nodeNo") Integer nodeNo, @Param("operatorId") String operatorId, @Param("action") String action, @Param("comment") String comment, @Param("operatedAt") long operatedAt);
    int countActiveByFlowDefinitionId(@Param("corpid") String corpid, @Param("flowDefinitionId") Long flowDefinitionId);
}
