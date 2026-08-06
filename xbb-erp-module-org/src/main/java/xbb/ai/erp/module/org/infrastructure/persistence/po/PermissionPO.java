package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_permission")
public class PermissionPO {

    private Long id;
    private Long parentId;
    private String permissionCode;
    private String permissionName;
    private String permissionType;
    private String menuAlias;
    private String actionCode;
    private Integer dataScopeFlag;
    private Integer permissionStatus;
    private String remark;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
