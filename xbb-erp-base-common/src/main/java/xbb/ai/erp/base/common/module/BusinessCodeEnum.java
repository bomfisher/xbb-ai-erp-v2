package xbb.ai.erp.base.common.module;

import lombok.Getter;

/**
 * 业务code枚举
 */
@Getter
public enum BusinessCodeEnum {
    ORG_MEMBER("ORG_MEMBER"),
    ORG_DEPARTMENT("ORG_DEPARTMENT"),
    CUSTOMER("CUSTOMER"),
    DEMO("DEMO"),
    DEMO_SUB("DEMO_SUB"),
    PRODUCT("PRODUCT"),
    SUPPLIER("SUPPLIER"),
    PURCHASE_REQUEST("PURCHASE_REQUEST"),
    PURCHASE_ORDER("PURCHASE_ORDER"),
    PURCHASE_PENDING_TASK("PURCHASE_PENDING_TASK"),
    PURCHASE_SOURCE_RELATION("PURCHASE_SOURCE_RELATION"),
    PURCHASE_REQUEST_ITEM("PURCHASE_REQUEST_ITEM"),
    PURCHASE_ORDER_ITEM("PURCHASE_ORDER_ITEM")
    ;

    private final String code;

    BusinessCodeEnum(String code) {
        this.code = code;
    }
}
