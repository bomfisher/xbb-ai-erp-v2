package xbb.ai.erp.module.demo.admin;

import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum DemoFieldEnum {
    NAME("main.name", "名称", FieldTypeEnum.TEXT, "name", true, null, null),
    USER_ID("main.userId", "员工", FieldTypeEnum.USER, "user_id", false, null, "ORG_MEMBER"),
    DEP_ID("main.depId", "部门", FieldTypeEnum.DEPT, "dep_id", false, null, "ORG_DEPARTMENT"),
    COMB("main.comb", "下拉", FieldTypeEnum.COMB, "comb", false, "0:禁用, 1:启用", null),
    COMB_MULTI("main.combMulti", "下拉多选", FieldTypeEnum.COMB_MULTI, "comb_multi", false, "0:禁用1, 1:启用2", null),
    NUM_INT("main.numInt", "整数", FieldTypeEnum.NUM_INT, "num_int", false, null, null),
    NUM_DOUBLE("main.numDouble", "浮点", FieldTypeEnum.NUM_DOUBLE, "num_double", false, null, null),
    AMOUNT("main.amount", "金额", FieldTypeEnum.AMOUNT, "amount", false, null, null),
    DATE("main.date", "日期", FieldTypeEnum.DATE, "date", false, null, null),
    TIME("main.time", "时间", FieldTypeEnum.TIME, "time", false, null, null),
    FILE("main.file", "附件", FieldTypeEnum.FILE, null, false, null, null),
    IMAGE("main.image", "图片", FieldTypeEnum.IMAGE, null, false, null, null),
    ADDRESS("main.address", "地址", FieldTypeEnum.ADDRESS, null, false, null, null),
    CREATOR_ID("main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id", false, null, "ORG_MEMBER"),
    MODIFY_ID("main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id", false, null, "ORG_MEMBER");

    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final String filterName;
    private final Boolean required;
    private final String options;
    private final String businessCode;
    private final List<SceneTypeEnum> scenes = List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE);

    DemoFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, Boolean required, String options, String businessCode) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
        this.filterName = filterName;
        this.required = required;
        this.options = options;
        this.businessCode = businessCode;
    }

    public boolean supports(SceneTypeEnum scene) {
        return scenes.contains(scene);
    }
}
