package xbb.ai.erp.module.approval.contract;

/**
 * 审批提交结果。
 *
 * @param instanceId 审批实例标识；无需审批时可为空
 * @param status 提交后的审批状态
 * @param flowVersion 命中的流程版本；无需审批时可为空
 */
public record ApprovalSubmission(
    String instanceId,
    ApprovalStatus status,
    Integer flowVersion
) {
}
