package xbb.ai.erp.module.approval.domain.repository;

import xbb.ai.erp.module.approval.domain.model.ApprovalInstance;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceActionLog;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceTask;
import java.util.List;

public interface ApprovalInstanceRepository {
    ApprovalInstance findById(String corpid, String instanceId);
    ApprovalInstance findByRequestId(String corpid, String requestId);
    List<ApprovalInstanceTask> findTasks(String instanceId, Integer nodeNo);
    List<ApprovalInstance> findByApprovalCenter(String corpid, String userId, String view, String status,
                                                 String businessCode, String keyword);
    List<ApprovalInstanceActionLog> findActionLogs(String instanceId);
    boolean canAccessApprovalCenter(String corpid, String userId, String instanceId);
    void insert(ApprovalInstance instance);
    void insertTasks(List<ApprovalInstanceTask> tasks);
    void update(ApprovalInstance instance);
    void updateTask(ApprovalInstanceTask task);
    void activateTasks(String instanceId, Integer nodeNo, long activatedAt);
    void cancelTasks(String instanceId, Integer nodeNo, long handledAt);
    void appendAction(String instanceId, Integer nodeNo, String operatorId, String action, String comment, long operatedAt);
    boolean hasActiveByFlowDefinitionId(String corpid, Long flowDefinitionId);
}
