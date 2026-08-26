package xbb.ai.erp.module.approval.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceListDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceDetailVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceListItemVO;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceService;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstance;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceActionLog;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceTask;
import xbb.ai.erp.module.approval.domain.repository.ApprovalInstanceRepository;
import xbb.ai.erp.module.org.application.service.OrgListReferenceQueryService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * 审批中心实例查询用例。
 */
@Service
@RequiredArgsConstructor
public class ApprovalInstanceServiceImpl implements ApprovalInstanceService {

    private final ApprovalInstanceRepository instanceRepository;

    private final ObjectMapper objectMapper;

    private final OrgListReferenceQueryService orgListReferenceQueryService;

    private static final ZoneId DISPLAY_ZONE = ZoneId.of("Asia/Shanghai");

    private static final DateTimeFormatter DISPLAY_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ApprovalInstanceListItemVO> list(ApprovalInstanceListDTO dto) {
        requireContext(dto.getCorpid(), dto.getUserId());
        String view = normalizeView(dto.getView());
        String status = normalizeStatus(dto.getStatus());
        List<ApprovalInstance> instances = instanceRepository.findByApprovalCenter(dto.getCorpid(), dto.getUserId(), view,
            status, dto.getBusinessCode(), dto.getKeyword());
        Map<String, String> submitterNames = orgListReferenceQueryService.findActiveMemberNames(dto.getCorpid(),
            instances.stream().map(ApprovalInstance::getSubmitterId).collect(java.util.stream.Collectors.toSet()));
        return instances.stream()
            .map(instance -> toListItem(instance, submitterNames))
            .toList();
    }

    @Override
    public ApprovalInstanceDetailVO detail(ApprovalInstanceDetailDTO dto) {
        requireContext(dto.getCorpid(), dto.getUserId());
        if (dto.getInstanceId() == null || dto.getInstanceId().isBlank()) {
            throw new BizException("审批实例标识不能为空");
        }
        ApprovalInstance instance = instanceRepository.findById(dto.getCorpid(), dto.getInstanceId());
        if (instance == null) {
            throw new BizException("审批实例不存在");
        }
        if (!instanceRepository.canAccessApprovalCenter(dto.getCorpid(), dto.getUserId(), dto.getInstanceId())) {
            throw new BizException("无权查看该审批实例");
        }
        ApprovalInstanceDetailVO vo = new ApprovalInstanceDetailVO();
        copyListItem(instance, vo, orgListReferenceQueryService.findActiveMemberNames(dto.getCorpid(),
            Set.of(instance.getSubmitterId())));
        JsonNode snapshot = readFlowSnapshot(instance.getFlowSnapshotJson());
        vo.setFlowName(snapshot.path("flowName").asText(instance.getFlowCode()));
        vo.setFlowVersion(instance.getFlowVersion());
        vo.setScene(instance.getApprovalScene().name());
        vo.setSubjectSnapshotJson(instance.getSubjectSnapshotJson());
        vo.setTimeline(instanceRepository.findActionLogs(instance.getInstanceId()).stream()
            .map(actionLog -> toTimelineItem(actionLog, snapshot))
            .toList());
        vo.setFlowNodes(flowNodes(instance, snapshot, instanceRepository.findTasks(instance.getInstanceId(), null)));
        return vo;
    }

    @Override
    public boolean hasActiveInstancesByFlowDefinitionId(String corpid, Long flowDefinitionId) {
        return instanceRepository.hasActiveByFlowDefinitionId(corpid, flowDefinitionId);
    }

    private ApprovalInstanceListItemVO toListItem(ApprovalInstance instance) {
        return toListItem(instance, Map.of());
    }

    private ApprovalInstanceListItemVO toListItem(ApprovalInstance instance, Map<String, String> submitterNames) {
        ApprovalInstanceListItemVO vo = new ApprovalInstanceListItemVO();
        copyListItem(instance, vo, submitterNames);
        return vo;
    }

    private void copyListItem(ApprovalInstance instance, ApprovalInstanceListItemVO vo, Map<String, String> submitterNames) {
        JsonNode snapshot = readFlowSnapshot(instance.getFlowSnapshotJson());
        vo.setInstanceId(instance.getInstanceId());
        vo.setBusinessCode(instance.getBusinessCode());
        vo.setBusinessName(BusinessCodeEnum.chineseNameOf(instance.getBusinessCode()));
        vo.setSubjectId(instance.getSubjectId());
        vo.setSubjectSummary(instance.getSubjectSummary());
        Map<String, String> resolvedSubmitterNames = submitterNames == null ? Map.of() : submitterNames;
        vo.setSubmitterName(resolvedSubmitterNames.getOrDefault(instance.getSubmitterId(), instance.getSubmitterId()));
        vo.setStatus(toDisplayStatus(instance.getStatus()));
        vo.setCurrentNodeName(nodeName(snapshot, instance.getCurrentNodeNo()));
        vo.setSubmittedAt(formatTime(instance.getSubmittedAt()));
        vo.setCompletedAt(formatTime(instance.getCompletedAt()));
    }

    private ApprovalInstanceDetailVO.TimelineItemVO toTimelineItem(ApprovalInstanceActionLog actionLog,
                                                                     JsonNode snapshot) {
        ApprovalInstanceDetailVO.TimelineItemVO item = new ApprovalInstanceDetailVO.TimelineItemVO();
        item.setNodeName(nodeName(snapshot, actionLog.getNodeNo()));
        item.setOperatorName(actionLog.getOperatorId());
        item.setAction(actionName(actionLog.getAction()));
        item.setComment(actionLog.getComment());
        item.setOperatedAt(formatTime(actionLog.getOperatedAt()));
        return item;
    }

    private List<ApprovalInstanceDetailVO.FlowNodeVO> flowNodes(ApprovalInstance instance, JsonNode snapshot,
                                                                  List<ApprovalInstanceTask> tasks) {
        return StreamSupport.stream(snapshot.path("nodes").spliterator(), false).map(node -> {
            ApprovalInstanceDetailVO.FlowNodeVO item = new ApprovalInstanceDetailVO.FlowNodeVO();
            int nodeNo = node.path("nodeNo").asInt();
            item.setNodeNo(nodeNo);
            item.setNodeName(node.path("nodeName").asText("审批节点"));
            String frozenAssignees = tasks.stream()
                .filter(task -> Integer.valueOf(nodeNo).equals(task.getNodeNo()))
                .map(ApprovalInstanceTask::getAssigneeId)
                .distinct()
                .collect(java.util.stream.Collectors.joining("、"));
            item.setApproverSummary(frozenAssignees.isBlank() ? approverSummary(node.path("approvers"))
                : "已冻结审批人：" + frozenAssignees);
            item.setStatus(flowNodeStatus(instance, nodeNo));
            return item;
        }).toList();
    }

    private String approverSummary(JsonNode approvers) {
        List<String> values = StreamSupport.stream(approvers.spliterator(), false).map(approver -> {
            String type = approver.path("approverType").asText();
            return switch (type) {
                case "USER" -> "指定人员：" + approver.path("approverValue").asText();
                case "ROLE" -> "指定角色：" + approver.path("approverValue").asText();
                case "SUPERIOR" -> approver.path("superiorLevel").asInt(1) + "级主管";
                default -> "审批人未配置";
            };
        }).toList();
        return String.join("；", values);
    }

    private String flowNodeStatus(ApprovalInstance instance, int nodeNo) {
        if (instance.getStatus() == ApprovalStatus.APPROVED) {
            return "APPROVED";
        }
        if (instance.getStatus() == ApprovalStatus.REJECTED || instance.getStatus() == ApprovalStatus.WITHDRAWN) {
            return instance.getCurrentNodeNo() != null && nodeNo < instance.getCurrentNodeNo() ? "APPROVED" : "CANCELLED";
        }
        if (instance.getCurrentNodeNo() == null || nodeNo < instance.getCurrentNodeNo()) {
            return "APPROVED";
        }
        return nodeNo == instance.getCurrentNodeNo() ? "PROCESSING" : "PENDING";
    }

    private JsonNode readFlowSnapshot(String flowSnapshotJson) {
        try {
            return objectMapper.readTree(flowSnapshotJson);
        } catch (JsonProcessingException | IllegalArgumentException exception) {
            throw new BizException("审批流程快照数据损坏");
        }
    }

    private String nodeName(JsonNode snapshot, Integer nodeNo) {
        if (nodeNo == null) {
            return "提交申请";
        }
        for (JsonNode node : snapshot.path("nodes")) {
            if (nodeNo.equals(node.path("nodeNo").asInt())) {
                return node.path("nodeName").asText("审批节点");
            }
        }
        return "审批节点";
    }

    private String normalizeView(String view) {
        if (view == null || view.isBlank()) {
            return "TODO";
        }
        if (!"TODO".equals(view) && !"SUBMITTED".equals(view) && !"COMPLETED".equals(view)) {
            throw new BizException("审批中心视图不合法");
        }
        return view;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return switch (status) {
            case "PENDING" -> ApprovalStatus.PENDING_APPROVAL.name();
            case "IN_PROGRESS" -> ApprovalStatus.IN_APPROVAL.name();
            case "APPROVED", "REJECTED", "WITHDRAWN" -> status;
            default -> throw new BizException("审批状态筛选不合法");
        };
    }

    private String toDisplayStatus(ApprovalStatus status) {
        return switch (status) {
            case PENDING_APPROVAL -> "PENDING";
            case IN_APPROVAL -> "IN_PROGRESS";
            case APPROVED, REJECTED, WITHDRAWN -> status.name();
            case NO_APPROVAL -> throw new BizException("无需审批的单据不应生成审批实例");
        };
    }

    private String actionName(String action) {
        return switch (action) {
            case "SUBMIT" -> "提交审批";
            case "AUTO_APPROVE" -> "自动通过";
            case "APPROVE" -> "同意";
            case "REJECT" -> "拒绝";
            case "WITHDRAW" -> "撤回";
            default -> action;
        };
    }

    private String formatTime(Long time) {
        if (time == null) {
            return null;
        }
        return DISPLAY_TIME_FORMATTER.format(Instant.ofEpochMilli(time).atZone(DISPLAY_ZONE));
    }

    private void requireContext(String corpid, String userId) {
        if (corpid == null || corpid.isBlank()) {
            throw new BizException("租户标识不能为空");
        }
        if (userId == null || userId.isBlank()) {
            throw new BizException("当前用户标识不能为空");
        }
    }
}
