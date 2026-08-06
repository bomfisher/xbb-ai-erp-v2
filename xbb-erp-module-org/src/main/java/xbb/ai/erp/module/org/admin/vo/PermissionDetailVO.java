package xbb.ai.erp.module.org.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDetailVO {
    private Long id;
    private Long parentId;
    private String permissionCode;
    private String permissionName;
    private String permissionType;
    private String menuAlias;
    private String actionCode;
    private Integer dataScopeFlag;
    private Integer permissionStatus;
    private Integer selected;
    private String dataScopeType;
    private List<PermissionDetailVO> children = new ArrayList<>();
}
