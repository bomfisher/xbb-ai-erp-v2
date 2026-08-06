package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class EmployeePO {

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
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
