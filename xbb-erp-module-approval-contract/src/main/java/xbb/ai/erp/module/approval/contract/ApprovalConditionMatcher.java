package xbb.ai.erp.module.approval.contract;

/**
 * 对业务快照执行结构化条件匹配的通用端口。
 */
public interface ApprovalConditionMatcher {

    boolean matches(ApprovalSubjectSchema schema, ApprovalConditionBranch branch, String snapshotJson);
}
