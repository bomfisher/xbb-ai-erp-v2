package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum DocumentStatusEnum implements StatusOptionEnum {
    OPEN(0, "未关闭"),
    CLOSED(1, "已关闭"),
    MANUALLY_CLOSED(2, "手动关闭");

    private final Integer code;
    private final String name;

    DocumentStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
