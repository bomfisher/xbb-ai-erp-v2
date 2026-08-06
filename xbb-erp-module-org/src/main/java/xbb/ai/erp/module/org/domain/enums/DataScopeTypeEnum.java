package xbb.ai.erp.module.org.domain.enums;

import lombok.Getter;

@Getter
public enum DataScopeTypeEnum {
    SELF("SELF", "本人"),
    DEPT("DEPT", "本部门"),
    DEPT_AND_CHILD("DEPT_AND_CHILD", "本部门及子部门"),
    ALL("ALL", "全部");

    private final String code;
    private final String name;

    DataScopeTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
