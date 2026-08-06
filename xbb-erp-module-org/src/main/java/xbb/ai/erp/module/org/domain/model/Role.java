package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class Role {
    private Long id;
    private String corpid;
    private String roleName;
    private String roleType;
    private Integer roleStatus;
    private String remark;
}
