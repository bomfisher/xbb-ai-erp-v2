package xbb.ai.erp.module.demo.sub.application.assembler;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.common.application.render.ListRenderFieldEntity;
import xbb.ai.erp.module.demo.sub.admin.DemoSubFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

public final class DemoSubFieldAssembler {
  private DemoSubFieldAssembler() {}

  public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> fields) {
    return fields.stream().map(DemoSubFieldAssembler::buildListField).toList();
  }

  public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> fields, String corpid) {
    return fields.stream()
        .map(
            field -> {
              FieldEntity entity = SceneFieldAssembler.build(field);
              if (DemoSubFieldEnum.DATA_ID.getAttr().equals(entity.getAttr())) {
                entity.setBusinessSelectConfig(buildDemoBusinessSelectConfig(corpid));
              }
              if (DemoSubFieldEnum.USER_ID.getAttr().equals(entity.getAttr())) {
                entity.setBusinessSelectConfig(buildMemberSelectConfig(corpid));
              }
              if (DemoSubFieldEnum.DEPARTMENT_ID.getAttr().equals(entity.getAttr())) {
                entity.setBusinessSelectConfig(buildDepartmentSelectConfig(corpid));
              }
              return entity;
            })
        .toList();
  }

  private static FieldEntity buildListField(SceneFieldMeta field) {
    FieldEntity source = SceneFieldAssembler.build(field);
    ListRenderFieldEntity entity = new ListRenderFieldEntity();
    entity.setAttr(source.getAttr());
    entity.setAttrName(source.getAttrName());
    entity.setFieldType(source.getFieldType());
    entity.setRequired(source.getRequired());
    entity.setEditable(source.getEditable());
    entity.setItemList(source.getItemList());
    entity.setSubField(source.getSubField());
    if (DemoSubFieldEnum.DATA_NAME.getAttr().equals(entity.getAttr())) {
      entity.setRenderValueAttr(DemoSubFieldEnum.DATA_ID.getAttr());
      entity.setBusinessSelectConfig(buildListBusinessSelectConfig(DemoSubFieldEnum.DATA_NAME.getBusinessCode()));
    }
    if (DemoSubFieldEnum.USER_ID.getAttr().equals(entity.getAttr())
        || DemoSubFieldEnum.CREATOR_ID.getAttr().equals(entity.getAttr())
        || DemoSubFieldEnum.MODIFY_ID.getAttr().equals(entity.getAttr())) {
      entity.setBusinessSelectConfig(buildListBusinessSelectConfig(DemoSubFieldEnum.USER_ID.getBusinessCode()));
    }
    if (DemoSubFieldEnum.DEPARTMENT_ID.getAttr().equals(entity.getAttr())) {
      entity.setBusinessSelectConfig(
          buildListBusinessSelectConfig(DemoSubFieldEnum.DEPARTMENT_ID.getBusinessCode()));
    }
    return entity;
  }

  private static FieldEntity.BusinessSelectConfig buildListBusinessSelectConfig(String businessCode) {
    FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
    config.setBusinessCode(businessCode);
    return config;
  }

  public static FieldEntity.BusinessSelectConfig buildDemoBusinessSelectConfig(String corpid) {
    FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
    config.setBusinessType(DemoSubFieldEnum.DATA_ID.getBusinessCode().toLowerCase());
    config.setBusinessCode(DemoSubFieldEnum.DATA_ID.getBusinessCode());
    config.setRequestPayload(
        Map.of("corpid", corpid, "businessCode", DemoSubFieldEnum.DATA_ID.getBusinessCode()));
    config.setPlaceholder("请选择关联DEMO数据");
    config.setDialogTitle("选择关联DEMO数据");
    config.setMultiple(Boolean.FALSE);
    return config;
  }

  public static FieldEntity.BusinessSelectConfig buildMemberSelectConfig(String corpid) {
    return buildOrgSelectConfig(
        "member", "ORG_MEMBER", "/erp/v1/org/memberSelect", corpid, "请选择成员", "选择成员");
  }

  public static FieldEntity.BusinessSelectConfig buildDepartmentSelectConfig(String corpid) {
    return buildOrgSelectConfig(
        "department", "ORG_DEPARTMENT", "/erp/v1/org/departmentSelect", corpid, "请选择部门", "选择部门");
  }

  private static FieldEntity.BusinessSelectConfig buildOrgSelectConfig(
      String businessType,
      String businessCode,
      String apiPrefix,
      String corpid,
      String placeholder,
      String dialogTitle) {
    FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
    config.setBusinessType(businessType);
    config.setBusinessCode(businessCode);
    config.setQuickSearchUrl(apiPrefix + "/quickSearch");
    config.setDialogSearchUrl(apiPrefix + "/dialogSearch");
    config.setGetByIdUrl(apiPrefix + "/getById");
    config.setRequestPayload(corpid == null || corpid.isBlank() ? Map.of() : Map.of("corpid", corpid));
    config.setPlaceholder(placeholder);
    config.setDialogTitle(dialogTitle);
    config.setMultiple(Boolean.FALSE);
    return config;
  }
}
