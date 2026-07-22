package xbb.ai.erp.base.common.filed;

import lombok.Data;

import java.util.List;

@Data
public class FieldEntity {
    //字段别名 用于接口传递
    private String attr;
    //字段名 用于名称展示
    private String attrName;
    //字段类型
    private String fieldType;
    //是否必填
    private Integer required;
    //允许编辑
    private Integer editable;
    //选项 下拉单选 多选 复选框 单选框都可以用
    private List<FieldItem> itemList;
}
