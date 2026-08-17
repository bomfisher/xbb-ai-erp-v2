package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum implements StatusOptionEnum {
    NOT_PAID(0, "未付款"),
    PARTIALLY_PAID(1, "部分付款"),
    FULLY_PAID(2, "全部付款");

    private final Integer code;
    private final String name;

    PaymentStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
