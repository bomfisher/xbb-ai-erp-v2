package xbb.ai.erp.base.common.module;

import lombok.Getter;

/**
 * 业务code枚举
 */
@Getter
public enum BusinessCodeEnum {
    ORG_MEMBER("ORG_MEMBER"),
    ORG_DEPARTMENT("ORG_DEPARTMENT"),
    DEMO("DEMO"),
    DEMO_ITEM("DEMO_ITEM"),
    DEMO_SUB("DEMO_SUB"),
    CUSTOMER("CUSTOMER"),
    SUPPLIER("SUPPLIER"),
    WAREHOUSE("WAREHOUSE"),
    PURCHASE_ORDER("PURCHASE_ORDER"),
    PURCHASE_INBOUND("PURCHASE_INBOUND"),
    ;

    private final String code;

    BusinessCodeEnum(String code) {
        this.code = code;
    }
}
