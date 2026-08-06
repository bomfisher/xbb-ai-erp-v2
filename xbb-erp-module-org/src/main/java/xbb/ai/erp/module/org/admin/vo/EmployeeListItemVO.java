package xbb.ai.erp.module.org.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeListItemVO {
    private String id;
    private String userCode;
    private String userName;
    private String email;
    private String jobNo;
    private Long mainDepartmentId;
    private String mainDepartmentName;
    private String employmentStatus;
    private Integer userStatus;
    private List<Long> roleIdList;
    private List<String> roleNameList;
}
