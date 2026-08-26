package xbb.ai.erp.module.approval.contract;

/**
 * 审批平台向业务系统投递的结束事件。
 *
 * @param eventId 审批平台事件幂等键
 * @param instanceId 审批实例标识
 * @param tenantId 租户标识
 * @param businessCode 业务对象编码
 * @param scene 审批场景
 * @param subjectId 编辑审批关联的原业务对象标识；新建审批可为空
 * @param result 审批结束结果
 * @param subjectSnapshotJson 提交时冻结的业务数据快照
 * @param flowVersion 实例绑定的流程版本
 */
public record ApprovalResultEvent(
    String eventId,
    String instanceId,
    String tenantId,
    String businessCode,
    ApprovalScene scene,
    String subjectId,
    ApprovalResult result,
    String subjectSnapshotJson,
    Integer flowVersion
) {
}
