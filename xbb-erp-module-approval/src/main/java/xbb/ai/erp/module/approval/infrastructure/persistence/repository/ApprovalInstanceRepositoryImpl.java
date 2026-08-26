package xbb.ai.erp.module.approval.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstance;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceActionLog;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceTask;
import xbb.ai.erp.module.approval.domain.repository.ApprovalInstanceRepository;
import xbb.ai.erp.module.approval.infrastructure.persistence.mapper.ApprovalInstanceMapper;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstanceActionLogPO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstancePO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalInstanceTaskPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApprovalInstanceRepositoryImpl implements ApprovalInstanceRepository {

    private final ApprovalInstanceMapper mapper;

    @Override
    public ApprovalInstance findById(String corpid, String instanceId) {
        return toDomain(mapper.findById(corpid, instanceId));
    }

    @Override
    public ApprovalInstance findByRequestId(String corpid, String requestId) {
        return toDomain(mapper.findByRequestId(corpid, requestId));
    }

    @Override
    public List<ApprovalInstanceTask> findTasks(String instanceId, Integer nodeNo) {
        return mapper.findTasks(instanceId, nodeNo).stream().map(this::toTask).toList();
    }

    @Override
    public List<ApprovalInstance> findByApprovalCenter(String corpid, String userId, String view, String status,
                                                        String businessCode, String keyword) {
        return mapper.findByApprovalCenter(corpid, userId, view, status, businessCode, keyword).stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public List<ApprovalInstanceActionLog> findActionLogs(String instanceId) {
        return mapper.findActionLogs(instanceId).stream().map(this::toActionLog).toList();
    }

    @Override
    public boolean canAccessApprovalCenter(String corpid, String userId, String instanceId) {
        return mapper.countApprovalCenterAccess(corpid, userId, instanceId) > 0;
    }

    @Override
    public void insert(ApprovalInstance instance) {
        ApprovalInstancePO po = toPO(instance);
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        mapper.insert(po);
    }

    @Override
    public void insertTasks(List<ApprovalInstanceTask> tasks) {
        long now = System.currentTimeMillis();
        for (ApprovalInstanceTask task : tasks) {
            ApprovalInstanceTaskPO po = new ApprovalInstanceTaskPO();
            po.setId(null);
            po.setInstanceId(task.getInstanceId());
            po.setNodeNo(task.getNodeNo());
            po.setAssigneeId(task.getAssigneeId());
            po.setStatus(task.getStatus());
            po.setComment(task.getComment());
            po.setHandledAt(task.getHandledAt());
            po.setDel(0);
            po.setAddTime(now);
            po.setUpdateTime(now);
            mapper.insertTask(po);
            task.setId(po.getId());
        }
    }

    @Override
    public void update(ApprovalInstance instance) {
        ApprovalInstancePO po = toPO(instance);
        po.setUpdateTime(System.currentTimeMillis());
        mapper.update(po);
    }

    @Override
    public void updateTask(ApprovalInstanceTask task) {
        ApprovalInstanceTaskPO po = new ApprovalInstanceTaskPO();
        po.setId(task.getId());
        po.setStatus(task.getStatus());
        po.setComment(task.getComment());
        po.setHandledAt(task.getHandledAt());
        po.setUpdateTime(System.currentTimeMillis());
        mapper.updateTask(po);
    }

    @Override
    public void activateTasks(String instanceId, Integer nodeNo, long activatedAt) {
        mapper.activateTasks(instanceId, nodeNo, activatedAt);
    }

    @Override
    public void cancelTasks(String instanceId, Integer nodeNo, long handledAt) {
        mapper.cancelTasks(instanceId, nodeNo, handledAt);
    }

    @Override
    public void appendAction(String instanceId, Integer nodeNo, String operatorId, String action, String comment, long operatedAt) {
        mapper.appendAction(instanceId, nodeNo, operatorId, action, comment, operatedAt);
    }

    @Override
    public boolean hasActiveByFlowDefinitionId(String corpid, Long flowDefinitionId) {
        return mapper.countActiveByFlowDefinitionId(corpid, flowDefinitionId) > 0;
    }

    private ApprovalInstance toDomain(ApprovalInstancePO po) {
        if (po == null) {
            return null;
        }
        ApprovalInstance instance = new ApprovalInstance();
        instance.setInstanceId(po.getInstanceId());
        instance.setCorpid(po.getCorpid());
        instance.setFlowDefinitionId(po.getFlowDefinitionId());
        instance.setFlowCode(po.getFlowCode());
        instance.setFlowVersion(po.getFlowVersion());
        instance.setBusinessCode(po.getBusinessCode());
        instance.setApprovalScene(ApprovalScene.valueOf(po.getApprovalScene()));
        instance.setSubjectId(po.getSubjectId());
        instance.setRequestId(po.getRequestId());
        instance.setSubmitterId(po.getSubmitterId());
        instance.setSubjectSummary(po.getSubjectSummary());
        instance.setSubjectSnapshotJson(po.getSubjectSnapshotJson());
        instance.setFlowSnapshotJson(po.getFlowSnapshotJson());
        instance.setStatus(ApprovalStatus.valueOf(po.getStatus()));
        instance.setCurrentNodeNo(po.getCurrentNodeNo());
        instance.setSubmittedAt(po.getSubmittedAt());
        instance.setCompletedAt(po.getCompletedAt());
        return instance;
    }

    private ApprovalInstanceTask toTask(ApprovalInstanceTaskPO po) {
        ApprovalInstanceTask task = new ApprovalInstanceTask();
        task.setId(po.getId());
        task.setInstanceId(po.getInstanceId());
        task.setNodeNo(po.getNodeNo());
        task.setAssigneeId(po.getAssigneeId());
        task.setStatus(po.getStatus());
        task.setComment(po.getComment());
        task.setHandledAt(po.getHandledAt());
        return task;
    }

    private ApprovalInstanceActionLog toActionLog(ApprovalInstanceActionLogPO po) {
        ApprovalInstanceActionLog actionLog = new ApprovalInstanceActionLog();
        actionLog.setId(po.getId());
        actionLog.setInstanceId(po.getInstanceId());
        actionLog.setNodeNo(po.getNodeNo());
        actionLog.setOperatorId(po.getOperatorId());
        actionLog.setAction(po.getAction());
        actionLog.setComment(po.getComment());
        actionLog.setOperatedAt(po.getOperatedAt());
        return actionLog;
    }

    private ApprovalInstancePO toPO(ApprovalInstance instance) {
        ApprovalInstancePO po = new ApprovalInstancePO();
        po.setInstanceId(instance.getInstanceId());
        po.setCorpid(instance.getCorpid());
        po.setFlowDefinitionId(instance.getFlowDefinitionId());
        po.setFlowCode(instance.getFlowCode());
        po.setFlowVersion(instance.getFlowVersion());
        po.setBusinessCode(instance.getBusinessCode());
        po.setApprovalScene(instance.getApprovalScene().name());
        po.setSubjectId(instance.getSubjectId());
        po.setRequestId(instance.getRequestId());
        po.setSubmitterId(instance.getSubmitterId());
        po.setSubjectSummary(instance.getSubjectSummary());
        po.setSubjectSnapshotJson(instance.getSubjectSnapshotJson());
        po.setFlowSnapshotJson(instance.getFlowSnapshotJson());
        po.setStatus(instance.getStatus().name());
        po.setCurrentNodeNo(instance.getCurrentNodeNo());
        po.setSubmittedAt(instance.getSubmittedAt());
        po.setCompletedAt(instance.getCompletedAt());
        return po;
    }
}
