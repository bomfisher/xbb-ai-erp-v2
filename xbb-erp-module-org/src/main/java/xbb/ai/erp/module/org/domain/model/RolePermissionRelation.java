package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class RolePermissionRelation {
    private Long id;
    private String corpid;
    private Long roleId;
    private Long permissionId;
    private Integer relStatus;
}
