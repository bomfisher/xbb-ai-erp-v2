package xbb.ai.erp.module.approval.contract;

/**
 * 审批流程匹配结果。flowDefinitionId 为 null 表示无需审批。
 */
public record ApprovalFlowMatch(
    Long flowDefinitionId,
    String flowCode,
    Integer flowVersion
) {

    public static ApprovalFlowMatch noApproval() {
        return new ApprovalFlowMatch(null, null, null);
    }

    public boolean matched() {
        return flowDefinitionId != null;
    }
}
