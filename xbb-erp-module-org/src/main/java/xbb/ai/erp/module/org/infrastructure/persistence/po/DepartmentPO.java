package xbb.ai.erp.module.org.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_department")
public class DepartmentPO {

    private Long id;
    private String corpid;
    private String departmentCode;
    private String departmentName;
    private Long parentId;
    private String ancestorPath;
    private Integer departmentLevel;
    private Long leaderUserId;
    private Integer sortNo;
    private Integer departmentStatus;
    private String remark;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
