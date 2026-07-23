package xbb.ai.erp.base.common.filed;

import lombok.Data;

import java.util.List;

@Data
public class FieldEntity {
    private String attr;
    private String attrName;
    private String fieldType;
    private Integer required;
    private Integer editable;
    private List<FieldItem> itemList;
}
