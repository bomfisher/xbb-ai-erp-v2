package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class EmployeeDepartmentRelation {
    private Long id;
    private String corpid;
    private Long userId;
    private Long departmentId;
    private Integer mainFlag;
    private Integer relStatus;
}
