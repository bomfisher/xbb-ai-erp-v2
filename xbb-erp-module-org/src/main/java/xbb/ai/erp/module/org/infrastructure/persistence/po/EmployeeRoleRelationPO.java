package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_role_rel")
public class EmployeeRoleRelationPO {

    private Long id;
    private String corpid;
    private Long userId;
    private Long roleId;
    private Integer relStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
