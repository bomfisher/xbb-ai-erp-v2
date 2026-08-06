package xbb.ai.erp.module.org.admin.vo;

import lombok.Data;

@Data
public class DepartmentTreeItemVO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private Long parentId;
    private Integer departmentLevel;
    private Integer departmentStatus;
    private java.util.List<DepartmentTreeItemVO> children = new java.util.ArrayList<>();
}
