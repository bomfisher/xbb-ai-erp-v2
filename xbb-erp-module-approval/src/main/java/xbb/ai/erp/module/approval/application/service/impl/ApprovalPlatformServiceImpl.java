package xbb.ai.erp.module.approval.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceActionDTO;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceCommandService;
import xbb.ai.erp.module.approval.application.service.ApprovalApproverResolver;
import xbb.ai.erp.module.approval.contract.ApprovalFlowMatch;
import xbb.ai.erp.module.approval.contract.ApprovalFlowMatchApi;
import xbb.ai.erp.module.approval.contract.ApprovalPlatformApi;
import xbb.ai.erp.module.approval.contract.ApprovalResult;
import xbb.ai.erp.module.approval.contract.ApprovalResultEvent;
import xbb.ai.erp.module.approval.contract.ApprovalResultHandler;
import xbb.ai.erp.module.approval.contract.ApprovalProgressEvent;
import xbb.ai.erp.module.approval.contract.ApprovalProgressHandler;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.contract.ApprovalSubmission;
import xbb.ai.erp.module.approval.contract.ApprovalSubmitCommand;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowNode;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstance;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceTask;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeMode;
import xbb.ai.erp.module.approval.domain.repository.ApprovalFlowRepository;
import xbb.ai.erp.module.approval.domain.repository.ApprovalInstanceRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovalPlatformServiceImpl implements ApprovalPlatformApi, ApprovalInstanceCommandService {

    private final ApprovalFlowMatchApi flowMatchApi;
    private final ApprovalFlowRepository flowRepository;
    private final ApprovalInstanceRepository instanceRepository;
    private final ApprovalApproverResolver approverResolver;
    private final ObjectProvider<ApprovalResultHandler> resultHandlerProvider;
    private final ObjectProvider<ApprovalProgressHandler> progressHandlerProvider;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalSubmission submit(ApprovalSubmitCommand command) {
        ApprovalInstance existing = instanceRepository.findByRequestId(command.tenantId(), command.requestId());
        if (existing != null) {
            return new ApprovalSubmission(existing.getInstanceId(), existing.getStatus(), existing.getFlowVersion());
        }
        ApprovalFlowMatch match = flowMatchApi.match(command);
        if (!match.matched()) {
            return new ApprovalSubmission(null, ApprovalStatus.NO_APPROVAL, null);
        }
        ApprovalFlowDefinition flow = flowRepository.findById(command.tenantId(), match.flowDefinitionId());
        if (flow == null || flow.getNodes() == null || flow.getNodes().isEmpty()) {
            throw new BizException("审批流程不存在有效审批节点");
        }
        long now = System.currentTimeMillis();
        ApprovalInstance instance = new ApprovalInstance();
        instance.setInstanceId(UUID.randomUUID().toString());
        instance.setCorpid(command.tenantId());
        instance.setFlowDefinitionId(flow.getId());
        instance.setFlowCode(flow.getFlowCode());
        instance.setFlowVersion(flow.getVersion());
        instance.setBusinessCode(command.businessCode());
        instance.setApprovalScene(command.scene());
        instance.setSubjectId(command.subjectId());
        instance.setRequestId(command.requestId());
        instance.setSubmitterId(command.submitterId());
        instance.setSubjectSummary(command.subjectSummary());
        instance.setSubjectSnapshotJson(command.subjectSnapshotJson());
        instance.setFlowSnapshotJson(flowSnapshot(flow));
        instance.setStatus(ApprovalStatus.PENDING_APPROVAL);
        instance.setCurrentNodeNo(1);
        instance.setSubmittedAt(now);
        instanceRepository.insert(instance);
        instanceRepository.appendAction(instance.getInstanceId(), null, command.submitterId(), "SUBMIT", null, now);
        instanceRepository.insertTasks(freezeTasks(instance, flow));
        activateCurrentNode(instance, flow, now);
        return new ApprovalSubmission(instance.getInstanceId(), instance.getStatus(), instance.getFlowVersion());
    }

    @Override
    @Transactional
    public void approve(ApprovalInstanceActionDTO dto) {
        ApprovalInstance instance = requireActive(dto.getCorpid(), dto.getInstanceId());
        ApprovalInstanceTask task = instanceRepository.findTasks(instance.getInstanceId(), instance.getCurrentNodeNo()).stream()
            .filter(item -> "PENDING".equals(item.getStatus()) && dto.getUserId().equals(item.getAssigneeId()))
            .findFirst().orElseThrow(() -> new BizException("当前用户没有该审批任务"));
        task.setStatus("APPROVED");
        task.setComment(dto.getComment());
        task.setHandledAt(System.currentTimeMillis());
        instanceRepository.updateTask(task);
        instanceRepository.appendAction(instance.getInstanceId(), instance.getCurrentNodeNo(), dto.getUserId(), "APPROVE", dto.getComment(), task.getHandledAt());
        ApprovalFlowDefinition flow = flowRepository.findById(dto.getCorpid(), instance.getFlowDefinitionId());
        if (flow == null) {
            throw new BizException("审批流程不存在");
        }
        ApprovalFlowNode node = flow.getNodes().stream().filter(item -> item.getNodeNo().equals(instance.getCurrentNodeNo())).findFirst()
            .orElseThrow(() -> new BizException("审批节点不存在"));
        List<ApprovalInstanceTask> tasks = instanceRepository.findTasks(instance.getInstanceId(), instance.getCurrentNodeNo());
        boolean complete = node.getApprovalMode() == ApprovalNodeMode.ANY_SIGN
            || tasks.stream().allMatch(item -> "APPROVED".equals(item.getStatus()));
        if (complete) {
            instanceRepository.cancelTasks(instance.getInstanceId(), instance.getCurrentNodeNo(), task.getHandledAt());
            moveNext(instance, flow, task.getHandledAt());
        }
    }

    @Override
    @Transactional
    public void reject(ApprovalInstanceActionDTO dto) {
        ApprovalInstance instance = requireActive(dto.getCorpid(), dto.getInstanceId());
        ApprovalInstanceTask task = instanceRepository.findTasks(instance.getInstanceId(), instance.getCurrentNodeNo()).stream()
            .filter(item -> "PENDING".equals(item.getStatus()) && dto.getUserId().equals(item.getAssigneeId()))
            .findFirst().orElseThrow(() -> new BizException("当前用户没有该审批任务"));
        long now = System.currentTimeMillis();
        task.setStatus("REJECTED");
        task.setComment(dto.getComment());
        task.setHandledAt(now);
        instanceRepository.updateTask(task);
        instanceRepository.cancelTasks(instance.getInstanceId(), instance.getCurrentNodeNo(), now);
        instanceRepository.appendAction(instance.getInstanceId(), instance.getCurrentNodeNo(), dto.getUserId(), "REJECT", dto.getComment(), now);
        finish(instance, ApprovalStatus.REJECTED, ApprovalResult.REJECTED, now);
    }

    @Override
    @Transactional
    public void withdraw(String tenantId, String instanceId, String submitterId, String reason) {
        ApprovalInstance instance = requireActive(tenantId, instanceId);
        if (!submitterId.equals(instance.getSubmitterId())) {
            throw new BizException("仅审批提交人可以撤回");
        }
        long now = System.currentTimeMillis();
        instanceRepository.cancelTasks(instanceId, instance.getCurrentNodeNo(), now);
        instanceRepository.appendAction(instanceId, instance.getCurrentNodeNo(), submitterId, "WITHDRAW", reason, now);
        finish(instance, ApprovalStatus.WITHDRAWN, ApprovalResult.WITHDRAWN, now);
    }

    @Override
    public ApprovalStatus getStatus(String tenantId, String instanceId) {
        ApprovalInstance instance = instanceRepository.findById(tenantId, instanceId);
        if (instance == null) {
            throw new BizException("审批实例不存在");
        }
        return instance.getStatus();
    }

    @Override
    public boolean canParticipateInBusiness(String tenantId, String instanceId) {
        return getStatus(tenantId, instanceId).allowsBusinessParticipation();
    }

    private void activateCurrentNode(ApprovalInstance instance, ApprovalFlowDefinition flow, long now) {
        ApprovalFlowNode node = flow.getNodes().stream().filter(item -> item.getNodeNo().equals(instance.getCurrentNodeNo())).findFirst()
            .orElseThrow(() -> new BizException("审批节点不存在"));
        instanceRepository.activateTasks(instance.getInstanceId(), node.getNodeNo(), now);
        List<ApprovalInstanceTask> tasks = instanceRepository.findTasks(instance.getInstanceId(), node.getNodeNo()).stream()
            .filter(task -> "PENDING".equals(task.getStatus()))
            .toList();
        if (tasks.isEmpty()) {
            instanceRepository.appendAction(instance.getInstanceId(), node.getNodeNo(), null, "AUTO_APPROVE", "节点无可用审批人，自动通过", now);
            moveNext(instance, flow, now);
            return;
        }
    }

    private List<ApprovalInstanceTask> freezeTasks(ApprovalInstance instance, ApprovalFlowDefinition flow) {
        return flow.getNodes().stream().flatMap(node -> node.getApprovers().stream()
                .flatMap(rule -> approverResolver.resolve(instance.getCorpid(), instance.getSubmitterId(), rule).stream())
                .distinct()
                .map(userId -> task(instance.getInstanceId(), node.getNodeNo(), userId,
                    node.getNodeNo().equals(instance.getCurrentNodeNo()) ? "PENDING" : "WAITING")))
            .toList();
    }

    private void moveNext(ApprovalInstance instance, ApprovalFlowDefinition flow, long now) {
        int nextNodeNo = instance.getCurrentNodeNo() + 1;
        if (flow.getNodes().stream().noneMatch(node -> node.getNodeNo().equals(nextNodeNo))) {
            finish(instance, ApprovalStatus.APPROVED, ApprovalResult.APPROVED, now);
            return;
        }
        instance.setCurrentNodeNo(nextNodeNo);
        instance.setStatus(ApprovalStatus.IN_APPROVAL);
        instanceRepository.update(instance);
        activateCurrentNode(instance, flow, now);
        ApprovalProgressEvent event = new ApprovalProgressEvent(
            instance.getInstanceId() + ":IN_APPROVAL:" + nextNodeNo,
            instance.getInstanceId(), instance.getCorpid(), instance.getBusinessCode(), instance.getApprovalScene(),
            instance.getSubjectId(), nextNodeNo, instance.getSubjectSnapshotJson(), instance.getFlowVersion());
        progressHandlerProvider.orderedStream()
            .filter(handler -> handler.businessCode().equals(instance.getBusinessCode()))
            .forEach(handler -> handler.handle(event));
    }

    private void finish(ApprovalInstance instance, ApprovalStatus status, ApprovalResult result, long now) {
        instance.setStatus(status);
        instance.setCurrentNodeNo(null);
        instance.setCompletedAt(now);
        instanceRepository.update(instance);
        ApprovalResultEvent event = new ApprovalResultEvent(
            instance.getInstanceId() + ":" + result,
            instance.getInstanceId(), instance.getCorpid(), instance.getBusinessCode(), instance.getApprovalScene(),
            instance.getSubjectId(), result, instance.getSubjectSnapshotJson(), instance.getFlowVersion());
        resultHandlerProvider.orderedStream()
            .filter(handler -> handler.businessCode().equals(instance.getBusinessCode()))
            .forEach(handler -> handler.handle(event));
    }

    private ApprovalInstance requireActive(String corpid, String instanceId) {
        ApprovalInstance instance = instanceRepository.findById(corpid, instanceId);
        if (instance == null || !instance.getStatus().isActive()) {
            throw new BizException("当前审批实例不可处理");
        }
        return instance;
    }

    private ApprovalInstanceTask task(String instanceId, Integer nodeNo, String userId, String status) {
        ApprovalInstanceTask task = new ApprovalInstanceTask();
        task.setInstanceId(instanceId);
        task.setNodeNo(nodeNo);
        task.setAssigneeId(userId);
        task.setStatus(status);
        return task;
    }

    private String flowSnapshot(ApprovalFlowDefinition flow) {
        try {
            return objectMapper.writeValueAsString(flow);
        } catch (JsonProcessingException exception) {
            throw new BizException("审批流程快照生成失败");
        }
    }
}
