package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class EmployeeRoleRelation {
    private Long id;
    private String corpid;
    private Long userId;
    private Long roleId;
    private Integer relStatus;
}
