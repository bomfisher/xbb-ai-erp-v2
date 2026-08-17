package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum InboundStatusEnum implements StatusOptionEnum {
    NOT_INBOUNDED(0, "未入库"),
    PARTIALLY_INBOUNDED(1, "部分入库"),
    FULLY_INBOUNDED(2, "全部入库");

    private final Integer code;
    private final String name;

    InboundStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
