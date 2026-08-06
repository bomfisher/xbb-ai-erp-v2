package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_department_rel")
public class EmployeeDepartmentRelationPO {

    private Long id;
    private String corpid;
    private Long userId;
    private Long departmentId;
    private Integer mainFlag;
    private Integer relStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
