package xbb.ai.erp.base.common.module;

import lombok.Getter;

/**
 * 业务code枚举
 */
@Getter
public enum BusinessTypeEnum {
    CUSTOMER("CUSTOMER")
    ;
    private String code;

    BusinessTypeEnum(String code) {
        this.code = code;
    }
}
