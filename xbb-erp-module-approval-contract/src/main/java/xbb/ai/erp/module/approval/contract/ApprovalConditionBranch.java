package xbb.ai.erp.module.approval.contract;

import java.util.List;

/**
 * 条件节点的一个分支。分支内规则使用 AND 关系。
 */
public record ApprovalConditionBranch(
    String branchId,
    String branchName,
    List<ApprovalConditionRule> rules,
    boolean fallback
) {
}
