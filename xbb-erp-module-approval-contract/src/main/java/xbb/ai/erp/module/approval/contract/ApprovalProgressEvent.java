package xbb.ai.erp.module.approval.contract;

/**
 * 审批实例从首节点推进到后续节点时的状态事件。
 */
public record ApprovalProgressEvent(
    String eventId,
    String instanceId,
    String tenantId,
    String businessCode,
    ApprovalScene scene,
    String subjectId,
    Integer currentNodeNo,
    String subjectSnapshotJson,
    Integer flowVersion
) {
}
