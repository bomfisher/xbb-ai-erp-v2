package xbb.ai.erp.module.common.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuItemVO {
    private String menuCode;
    private String menuName;
    private String routePath;
    private String componentPath;
    private List<MenuItemVO> children;
}
