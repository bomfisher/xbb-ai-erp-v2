package xbb.ai.erp.module.org.application.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeSavePojo extends BaseDTO {

    private String id;
    private String userId;
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
    private List<Long> departmentIdList;
    private List<Long> roleIdList;
}
