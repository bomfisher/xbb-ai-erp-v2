package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

@Data
public class Department {
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
}
