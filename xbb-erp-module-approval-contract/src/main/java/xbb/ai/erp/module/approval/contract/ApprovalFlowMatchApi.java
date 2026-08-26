package xbb.ai.erp.module.approval.contract;

/**
 * 业务提交审批前的通用流程匹配入口。
 */
public interface ApprovalFlowMatchApi {

    ApprovalFlowMatch match(ApprovalSubmitCommand command);
}
