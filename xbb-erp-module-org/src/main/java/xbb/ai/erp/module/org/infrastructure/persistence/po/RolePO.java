package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class RolePO {

    private Long id;
    private String corpid;
    private String roleName;
    private String roleType;
    private Integer roleStatus;
    private String remark;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
