package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class Permission {
    private Long id;
    private Long parentId;
    private String permissionCode;
    private String permissionName;
    private String permissionType;
    private String menuAlias;
    private String actionCode;
    private Integer dataScopeFlag;
    private Integer permissionStatus;
    private String remark;
}
