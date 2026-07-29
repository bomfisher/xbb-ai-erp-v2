package xbb.ai.erp.base.common.module;

import lombok.Getter;

/**
 * 业务code枚举
 */
@Getter
public enum BusinessCodeEnum {
    CUSTOMER("CUSTOMER")
    ;

    private final String code;

    BusinessCodeEnum(String code) {
        this.code = code;
    }
}
