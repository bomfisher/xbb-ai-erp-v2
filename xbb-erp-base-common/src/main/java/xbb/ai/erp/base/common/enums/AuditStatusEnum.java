package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum AuditStatusEnum implements StatusOptionEnum {
    PENDING(0, "待审核"),
    PROCESSING(1, "审核中"),
    APPROVED(2, "已审核"),
    REJECTED(3, "已拒绝");

    private final Integer code;
    private final String name;

    AuditStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
