package xbb.ai.erp.base.common.enums;

import java.util.Arrays;

public interface StatusOptionEnum {
    Integer getCode();

    String getName();

    static String options(StatusOptionEnum[] statuses) {
        return Arrays.stream(statuses)
            .map(status -> status.getCode() + ":" + status.getName())
            .reduce((left, right) -> left + "," + right)
            .orElse("");
    }
}
