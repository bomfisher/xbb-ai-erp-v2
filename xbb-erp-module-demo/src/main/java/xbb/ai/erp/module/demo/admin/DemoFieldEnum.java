package xbb.ai.erp.module.demo.admin;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public enum DemoFieldEnum {
    NAME("name", "main.name", "名称", FieldTypeEnum.TEXT, "name"),
    USER_ID("user_id", "main.userId", "员工", FieldTypeEnum.USER, "user_id"),
    DEP_ID("dep_id", "main.depId", "部门", FieldTypeEnum.DEPT, "dep_id"),
    COMB("comb", "main.comb", "下拉", FieldTypeEnum.COMB, "comb", "1:信息1, 2:信息2"),
    COMB_MULTI("comb_multi", "main.combMulti", "下拉多选", FieldTypeEnum.COMB_MULTI, null, "1:D信息1, 2:D信息2"),
    NUM_INT("num_int", "main.numInt", "整数", FieldTypeEnum.NUM_INT, "num_int"),
    NUM_DOUBLE("num_double", "main.numDouble", "浮点", FieldTypeEnum.NUM_DOUBLE, "num_double"),
    AMOUNT("amount", "main.amount", "金额", FieldTypeEnum.AMOUNT, "amount"),
    DATE("date", "main.date", "日期", FieldTypeEnum.DATE, "date"),
    TIME("time", "main.time", "时间", FieldTypeEnum.TIME, "time"),
    FILE("file", "main.file", "附件", FieldTypeEnum.FILE, null),
    IMAGE("image", "main.image", "图片", FieldTypeEnum.IMAGE, null),
    ADDRESS("address", "main.address", "地址", FieldTypeEnum.ADDRESS, null),
    CREATOR_ID("creator_id", "main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id"),
    MODIFY_ID("modify_id", "main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id"),
    DEMO_ITEM("demoItem", "items", "明细", FieldTypeEnum.SUB_ITEM, null, null, Set.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), List.of(
        new SubField("name", "name", "名称", FieldTypeEnum.TEXT, true, true)
    ));

    private final String name;
    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final String filterName;
    private final String options;
    private final Set<SceneTypeEnum> scenes;
    private final List<SubField> subFields;

    DemoFieldEnum(String name, String attr, String attrName, FieldTypeEnum fieldType, String filterName) {
        this(name, attr, attrName, fieldType, filterName, null);
    }

    DemoFieldEnum(String name, String attr, String attrName, FieldTypeEnum fieldType, String filterName, String options) {
        this(name, attr, attrName, fieldType, filterName, options, Set.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), List.of());
    }

    DemoFieldEnum(String name, String attr, String attrName, FieldTypeEnum fieldType, String filterName, String options, Set<SceneTypeEnum> scenes, List<SubField> subFields) {
        this.name = name;
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
        this.filterName = filterName;
        this.options = options;
        this.scenes = Set.copyOf(scenes);
        this.subFields = List.copyOf(subFields);
    }

    public boolean supports(SceneTypeEnum sceneType) {
        return scenes.contains(sceneType);
    }

    public List<FieldItem> itemList() {
        if (options == null || options.isBlank()) {
            return List.of();
        }
        return Arrays.stream(options.split(","))
            .map(String::trim)
            .filter(item -> !item.isEmpty())
            .map(item -> item.split(":", 2))
            .filter(pair -> pair.length == 2)
            .map(pair -> {
                FieldItem fieldItem = new FieldItem();
                fieldItem.setValue(pair[0].trim());
                fieldItem.setText(pair[1].trim());
                return fieldItem;
            })
            .toList();
    }

    public record SubField(String name, String attr, String attrName, FieldTypeEnum fieldType, boolean required, boolean editable) {
    }
}
