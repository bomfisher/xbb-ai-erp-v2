package xbb.ai.erp.module.settlement.admin;

import lombok.Getter;

@Getter
public enum ReceivableWriteOffStatusEnum {
    UNWRITTEN_OFF(0),
    PARTIALLY_WRITTEN_OFF(1),
    WRITTEN_OFF(2),
    CLOSED(3);

    private final int value;

    ReceivableWriteOffStatusEnum(int value) {
        this.value = value;
    }
}
