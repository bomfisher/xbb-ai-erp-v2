package xbb.ai.erp.module.approval.contract;

/**
 * 业务系统调用审批平台的入口。
 *
 * 实现可以是同进程适配器、HTTP 客户端或消息网关；接口不暴露审批平台的持久化模型。
 */
public interface ApprovalPlatformApi {

    ApprovalSubmission submit(ApprovalSubmitCommand command);

    void withdraw(String tenantId, String instanceId, String submitterId, String reason);

    ApprovalStatus getStatus(String tenantId, String instanceId);

    boolean canParticipateInBusiness(String tenantId, String instanceId);

    /**
     * 按业务对象判断是否允许进入后续业务。
     */
    default boolean canParticipateInBusiness(String tenantId, String businessCode, String subjectId) {
        throw new UnsupportedOperationException("当前审批平台实现不支持按业务对象校验");
    }
}
