package xbb.ai.erp.module.approval.contract;

/**
 * 业务系统提交给审批平台的冻结审批对象。
 *
 * @param tenantId 租户标识
 * @param businessCode 接入方定义的业务对象编码
 * @param scene 新建或编辑审批场景
 * @param subjectId 编辑审批关联的原业务对象标识；新建审批可为空
 * @param requestId 接入方生成的幂等键
 * @param submitterId 提交人标识
 * @param subjectSummary 审批待办展示摘要
 * @param subjectSnapshotJson 冻结的完整业务数据快照
 */
public record ApprovalSubmitCommand(
    String tenantId,
    String businessCode,
    ApprovalScene scene,
    String subjectId,
    String requestId,
    String submitterId,
    String subjectSummary,
    String subjectSnapshotJson
) {
}
