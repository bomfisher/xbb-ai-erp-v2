package xbb.ai.erp.module.demo.sub.admin;

import java.util.Set;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum DemoSubFieldEnum {
  DATA_ID(
      "main.dataId",
      "data_id",
      FieldTypeEnum.BUSINESS,
      "data_id",
      "DEMO",
      Set.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE)),
  DATA_NAME(
      "main.dataName",
      "关联DEMO",
      FieldTypeEnum.TEXT,
      null,
      null,
      Set.of(SceneTypeEnum.LIST)),
  NAME(
      "main.name",
      "名称",
      FieldTypeEnum.TEXT,
      "name",
      null,
      Set.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE)),
  USER_ID(
      "main.userId",
      "成员",
      FieldTypeEnum.USER,
      "user_id",
      "ORG_MEMBER",
      Set.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE)),
  DEPARTMENT_ID(
      "main.departmentId",
      "部门",
      FieldTypeEnum.DEPT,
      "department_id",
      "ORG_DEPARTMENT",
      Set.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE)),
  CREATOR_ID("main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id", "ORG_MEMBER", Set.of(SceneTypeEnum.LIST)),
  MODIFY_ID("main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id", "ORG_MEMBER", Set.of(SceneTypeEnum.LIST));
  private final String attr;
  private final String attrName;
  private final Integer fieldType;
  private final String filterName;
  private final String businessCode;
  private final Set<SceneTypeEnum> scenes;

  DemoSubFieldEnum(
      String attr,
      String attrName,
      FieldTypeEnum type,
      String filterName,
      String businessCode,
      Set<SceneTypeEnum> scenes) {
    this.attr = attr;
    this.attrName = attrName;
    this.fieldType = type.getType();
    this.filterName = filterName;
    this.businessCode = businessCode;
    this.scenes = scenes;
  }

  public boolean supports(SceneTypeEnum scene) {
    return scenes.contains(scene);
  }
}
