package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum InvoiceStatusEnum implements StatusOptionEnum {
    NOT_INVOICED(0, "未开票"),
    PARTIALLY_INVOICED(1, "部分开票"),
    FULLY_INVOICED(2, "全部开票");

    private final Integer code;
    private final String name;

    InvoiceStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
