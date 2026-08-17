package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum OutboundStatusEnum implements StatusOptionEnum {
    NOT_OUTBOUNDED(0, "未出库"),
    PARTIALLY_OUTBOUNDED(1, "部分出库"),
    FULLY_OUTBOUNDED(2, "全部出库");

    private final Integer code;
    private final String name;

    OutboundStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
