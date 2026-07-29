package xbb.ai.erp.module.common.admin.pojo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.List;

@Data
public class FilterField {
    private String attr;
    private String attrName;
    private String fieldType;
    private List<String> supportedSymbols;
    private List<FieldItem> itemList;
}
