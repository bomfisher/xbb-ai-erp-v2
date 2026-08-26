package xbb.ai.erp.base.common.filed;

import lombok.Getter;

@Getter
public enum ProductSelectSourceModeEnum {
    MASTER_ONLY("MASTER_ONLY", "MASTER_DATA"),
    UPSTREAM_ONLY("UPSTREAM_ONLY", "UPSTREAM_DOCUMENT"),
    MIXED("MIXED", "MASTER_DATA");

    private final String code;
    private final String defaultSource;

    ProductSelectSourceModeEnum(String code, String defaultSource) {
        this.code = code;
        this.defaultSource = defaultSource;
    }
}
