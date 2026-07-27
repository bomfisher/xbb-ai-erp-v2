package xbb.ai.erp.module.common.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class MenuPO extends BaseEntity {
    private String corpid;
    private String menuCode;
    private String menuName;
    private String surface;
    private Long parentMenuId;
    private String menuType;
    private String routePath;
    private String componentPath;
    private String icon;
    private String pinnedHome;
    private Integer enableStatus;
    private String creatorId;
    private String modifyId;
}
