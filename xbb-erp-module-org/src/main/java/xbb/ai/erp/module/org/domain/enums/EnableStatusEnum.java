package xbb.ai.erp.module.org.domain.enums;

import lombok.Getter;

@Getter
public enum EnableStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "停用");

    private final Integer code;
    private final String name;

    EnableStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
