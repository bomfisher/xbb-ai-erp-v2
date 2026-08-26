package xbb.ai.erp.base.common.enums;

import lombok.Getter;

/**
 * 业务单据的审批状态。
 */
@Getter
public enum ApprovalStatusEnum implements StatusOptionEnum {
    PENDING(0, "待审批"),
    PROCESSING(1, "审批中"),
    APPROVED(2, "已审批"),
    REJECTED(3, "已拒绝"),
    NO_NEED_APPROVED(4, "无需审批");

    private final Integer code;

    private final String name;

    ApprovalStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean allowsDownstream(Integer approvalStatus) {
        return APPROVED.code.equals(approvalStatus) || NO_NEED_APPROVED.code.equals(approvalStatus);
    }
}
