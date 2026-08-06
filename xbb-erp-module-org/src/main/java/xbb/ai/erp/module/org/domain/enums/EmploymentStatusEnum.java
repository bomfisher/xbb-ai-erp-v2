package xbb.ai.erp.module.org.domain.enums;

import lombok.Getter;

@Getter
public enum EmploymentStatusEnum {
    ACTIVE("ACTIVE", "在职"),
    RESIGNED("RESIGNED", "离职");

    private final String code;
    private final String name;

    EmploymentStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
