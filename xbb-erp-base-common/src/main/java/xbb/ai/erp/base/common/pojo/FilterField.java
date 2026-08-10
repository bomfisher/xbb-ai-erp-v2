package xbb.ai.erp.base.common.pojo;

import lombok.Data;
import lombok.AccessLevel;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.List;

@Data
public class FilterField {
    private String attr;
    private String attrName;
    private String fieldType;
    /** 前端展示类型对应的筛选协议类型，提交条件时使用。 */
    private String filterFieldType;
    private List<String> supportedSymbols;
    private List<FieldItem> itemList;
    private FieldEntity.BusinessSelectConfig businessSelectConfig;
    @Getter(AccessLevel.NONE)
    private Integer sourceFieldType;

    public Integer sourceFieldType() {
        return sourceFieldType;
    }
}
