package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum CommonErrorCodeEnum {
    SYSTEM_ERROR(500, "系统异常"),
    BIZ_ERROR(400, "业务异常");

    private final Integer code;
    private final String message;

    CommonErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
