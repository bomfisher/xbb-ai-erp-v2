package xbb.ai.erp.module.demo.sub.application.assembler;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.demo.sub.admin.DemoSubFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

public final class DemoSubFieldAssembler {
  private DemoSubFieldAssembler() {}

  public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> fields) {
    return fields.stream().map(SceneFieldAssembler::build).toList();
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
    return buildOrgSelectConfig("member", "/erp/v1/org/memberSelect", corpid, "请选择成员", "选择成员");
  }

  public static FieldEntity.BusinessSelectConfig buildDepartmentSelectConfig(String corpid) {
    return buildOrgSelectConfig("department", "/erp/v1/org/departmentSelect", corpid, "请选择部门", "选择部门");
  }

  private static FieldEntity.BusinessSelectConfig buildOrgSelectConfig(
      String businessType, String apiPrefix, String corpid, String placeholder, String dialogTitle) {
    FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
    config.setBusinessType(businessType);
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
