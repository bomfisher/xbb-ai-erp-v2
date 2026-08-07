package xbb.ai.erp.base.common.pojo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.List;

@Data
public class FilterField {
    private String attr;
    private String attrName;
    private String fieldType;
    private List<String> supportedSymbols;
    private List<FieldItem> itemList;
    private FieldEntity.BusinessSelectConfig businessSelectConfig;
}
