package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Employee {
    private Long id;
    private String userId;
    private String corpid;
    private Long accountId;
    private String userCode;
    private String userName;
    private String email;
    private String jobNo;
    private Long mainDepartmentId;
    private Integer userStatus;
    private String employmentStatus;
    private Long entryTime;
    private Long resignedTime;
    private String remark;
    private List<EmployeeDepartmentRelation> departmentRelations;
    private List<EmployeeRoleRelation> roleRelations;
}
