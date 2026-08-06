package xbb.ai.erp.module.org.domain.enums;

import lombok.Getter;

@Getter
public enum PermissionTypeEnum {
    MENU("MENU", "菜单"),
    ACTION("ACTION", "操作");

    private final String code;
    private final String name;

    PermissionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
