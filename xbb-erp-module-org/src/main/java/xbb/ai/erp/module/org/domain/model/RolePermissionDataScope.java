package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class RolePermissionDataScope {
    private Long id;
    private String corpid;
    private Long roleId;
    private Long permissionId;
    private String dataScopeType;
    private Integer relStatus;
}
