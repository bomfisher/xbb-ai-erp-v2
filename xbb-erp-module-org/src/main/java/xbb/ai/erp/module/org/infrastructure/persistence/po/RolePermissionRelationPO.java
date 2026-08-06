package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_permission_rel")
public class RolePermissionRelationPO {

    private Long id;
    private String corpid;
    private Long roleId;
    private Long permissionId;
    private Integer relStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
