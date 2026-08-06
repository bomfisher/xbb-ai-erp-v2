package xbb.ai.erp.module.org.application.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RolePermissionSavePojo extends BaseDTO {

    private Long roleId;
    private List<MenuPermissionSaveItemPojo> permissionList;

    @Data
    public static class MenuPermissionSaveItemPojo {
        private Long menuPermissionId;
        private Integer selected;
        private String dataScopeType;
        private List<Long> actionPermissionIdList;
    }
}
