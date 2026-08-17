package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum ReceiptStatusEnum implements StatusOptionEnum {
    NOT_RECEIVED(0, "未收款"),
    PARTIALLY_RECEIVED(1, "部分收款"),
    FULLY_RECEIVED(2, "全部收款");

    private final Integer code;
    private final String name;

    ReceiptStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
