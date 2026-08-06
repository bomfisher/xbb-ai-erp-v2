package xbb.ai.erp.module.purchase.application.provider;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class PurchaseListMetaSupport {

    private PurchaseListMetaSupport() {
    }

    static final List<String> TEXT_SYMBOLS = List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY");
    static final List<String> ENUM_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    static final List<String> ID_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    static final List<String> DATE_SYMBOLS = List.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY");

    static Map<String, ListFilterMetaPojo> buildConditionMetaMap(List<FilterDefinition> definitions) {
        Map<String, ListFilterMetaPojo> metaMap = new LinkedHashMap<>();
        for (FilterDefinition definition : definitions) {
            metaMap.put(definition.attr(), new ListFilterMetaPojo(
                definition.attr(),
                definition.column(),
                definition.fieldType(),
                Set.copyOf(definition.supportedSymbols())
            ));
        }
        return Collections.unmodifiableMap(metaMap);
    }

    static FilterField buildFilterField(FilterDefinition definition) {
        FilterField field = new FilterField();
        field.setAttr(definition.attr());
        field.setAttrName(definition.attrName());
        field.setFieldType(definition.fieldType());
        field.setSupportedSymbols(definition.supportedSymbols());
        field.setItemList(definition.itemList());
        return field;
    }

    static FieldEntity buildHeader(String attr, String attrName) {
        FieldEntity field = new FieldEntity();
        field.setAttr(attr);
        field.setAttrName(attrName);
        field.setFieldType(String.valueOf(FieldTypeEnum.TEXT.getType()));
        field.setRequired(0);
        field.setEditable(1);
        field.setItemList(List.of());
        return field;
    }

    static ListButtonItemPojo buildButton(String buttonCode, String buttonName, Integer sort, String actionCode) {
        ListButtonItemPojo item = new ListButtonItemPojo();
        item.setButtonCode(buttonCode);
        item.setButtonName(buttonName);
        item.setSort(sort);
        item.setActionCode(actionCode);
        return item;
    }

    static ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
        ListRowActionItemPojo item = new ListRowActionItemPojo();
        item.setActionCode(actionCode);
        item.setActionName(actionName);
        item.setSort(sort);
        item.setShowMode(showMode);
        item.setConfirmType(confirmType);
        return item;
    }

    record FilterDefinition(
        String attr,
        String attrName,
        String fieldType,
        String column,
        List<String> supportedSymbols,
        List<FieldItem> itemList
    ) {
    }
}
