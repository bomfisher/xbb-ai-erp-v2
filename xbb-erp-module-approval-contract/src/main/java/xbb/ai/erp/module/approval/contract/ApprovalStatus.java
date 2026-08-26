package xbb.ai.erp.module.approval.contract;

/**
 * 审批平台的标准实例状态。
 */
public enum ApprovalStatus {
    NO_APPROVAL,
    PENDING_APPROVAL,
    IN_APPROVAL,
    APPROVED,
    REJECTED,
    WITHDRAWN;

    public boolean allowsBusinessParticipation() {
        return this == NO_APPROVAL || this == APPROVED;
    }

    public boolean isActive() {
        return this == PENDING_APPROVAL || this == IN_APPROVAL;
    }
}
