package xbb.ai.erp.module.supplier.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.supplier.application.support.SupplierFieldMetadataSupport;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SupplierListMetaProvider implements ListMetaProvider {

    private static final List<String> TEXT_SYMBOLS = List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> ENUM_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> USER_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<SupplierListFilterDefinition> FILTER_DEFINITIONS = List.of(
        new SupplierListFilterDefinition("supplierCode", "供应商编码", "TEXT", "supplier_code", TEXT_SYMBOLS, List.of(), null),
        new SupplierListFilterDefinition("supplierName", "供应商名称", "TEXT", "supplier_name", TEXT_SYMBOLS, List.of(), null),
        new SupplierListFilterDefinition("supplierCategory", "供应商分类", "ENUM", "supplier_category", ENUM_SYMBOLS, SupplierFieldMetadataSupport.supplierCategoryOptions(), null),
        new SupplierListFilterDefinition("mainBusinessCategory", "主营业务分类", "ENUM", "main_business_category", ENUM_SYMBOLS, SupplierFieldMetadataSupport.mainBusinessCategoryOptions(), null),
        new SupplierListFilterDefinition("ownerPurchaserId", "归属采购", "USER", "owner_purchaser_id", USER_SYMBOLS, List.of(), SupplierFieldMetadataSupport.memberSingleSelectConfig(null)),
        new SupplierListFilterDefinition("bizStatus", "业务状态", "ENUM", "biz_status", ENUM_SYMBOLS, SupplierFieldMetadataSupport.bizStatusOptions(), null),
        new SupplierListFilterDefinition("refStatus", "引用状态", "ENUM", "ref_status", ENUM_SYMBOLS, SupplierFieldMetadataSupport.refStatusOptions(), null)
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.SUPPLIER.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return FILTER_DEFINITIONS.stream().map(definition -> buildFilterField(definition, dto.getCorpid())).toList();
    }

    public static Map<String, ListFilterMetaPojo> conditionMetaMap() {
        return CONDITION_META_MAP;
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return conditionMetaMap();
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return List.of(
            buildTextHeader("main.supplierCode", "供应商编码"),
            buildTextHeader("main.supplierName", "供应商名称"),
            buildTextHeader("main.supplierShortName", "供应商简称"),
            buildEnumHeader("main.supplierCategory", "供应商分类", SupplierFieldMetadataSupport.supplierCategoryOptions()),
            buildEnumHeader("main.mainBusinessCategory", "主营业务分类", SupplierFieldMetadataSupport.mainBusinessCategoryOptions()),
            buildUserHeader("main.ownerPurchaserId", "归属采购", dto.getCorpid()),
            buildEnumHeader("main.bizStatus", "业务状态", SupplierFieldMetadataSupport.bizStatusOptions()),
            buildEnumHeader("main.refStatus", "引用状态", SupplierFieldMetadataSupport.refStatusOptions()),
            buildTextHeader("main.addTime", "创建时间"),
            buildTextHeader("main.updateTime", "更新时间")
        );
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(buildButton("ADD", "新增", 10, "ADD")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of(buildButton("EXPORT", "导出", 20, "EXPORT")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
        return bundle;
    }

    private static Map<String, ListFilterMetaPojo> buildConditionMetaMap(List<SupplierListFilterDefinition> definitions) {
        Map<String, ListFilterMetaPojo> metaMap = new LinkedHashMap<>();
        for (SupplierListFilterDefinition definition : definitions) {
            metaMap.put(definition.attr(), new ListFilterMetaPojo(
                definition.attr(),
                definition.column(),
                definition.fieldType(),
                Set.copyOf(definition.supportedSymbols())
            ));
        }
        return Collections.unmodifiableMap(metaMap);
    }

    private static FilterField buildFilterField(SupplierListFilterDefinition definition, String corpid) {
        FilterField field = new FilterField();
        field.setAttr(definition.attr());
        field.setAttrName(definition.attrName());
        field.setFieldType(definition.fieldType());
        field.setSupportedSymbols(definition.supportedSymbols());
        field.setItemList(definition.itemList());
        field.setBusinessSelectConfig("ownerPurchaserId".equals(definition.attr())
            ? SupplierFieldMetadataSupport.memberSingleSelectConfig(corpid)
            : definition.businessSelectConfig());
        return field;
    }

    private static FieldEntity buildTextHeader(String attr, String attrName) {
        FieldEntity field = createBaseHeader(attr, attrName);
        field.setFieldType(String.valueOf(FieldTypeEnum.TEXT.getType()));
        return field;
    }

    private static FieldEntity buildEnumHeader(String attr, String attrName, List<FieldItem> itemList) {
        FieldEntity field = createBaseHeader(attr, attrName);
        field.setFieldType(String.valueOf(FieldTypeEnum.COMB.getType()));
        field.setItemList(itemList);
        return field;
    }

    private static FieldEntity buildUserHeader(String attr, String attrName, String corpid) {
        FieldEntity field = createBaseHeader(attr, attrName);
        field.setFieldType(String.valueOf(FieldTypeEnum.USER.getType()));
        field.setBusinessSelectConfig(SupplierFieldMetadataSupport.memberSingleSelectConfig(corpid));
        return field;
    }

    private static FieldEntity createBaseHeader(String attr, String attrName) {
        FieldEntity field = new FieldEntity();
        field.setAttr(attr);
        field.setAttrName(attrName);
        field.setRequired(0);
        field.setEditable(1);
        field.setItemList(List.of());
        return field;
    }

    private static ListButtonItemPojo buildButton(String buttonCode, String buttonName, Integer sort, String actionCode) {
        ListButtonItemPojo item = new ListButtonItemPojo();
        item.setButtonCode(buttonCode);
        item.setButtonName(buttonName);
        item.setSort(sort);
        item.setActionCode(actionCode);
        return item;
    }

    private static ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
        ListRowActionItemPojo item = new ListRowActionItemPojo();
        item.setActionCode(actionCode);
        item.setActionName(actionName);
        item.setSort(sort);
        item.setShowMode(showMode);
        item.setConfirmType(confirmType);
        return item;
    }

    private record SupplierListFilterDefinition(
        String attr,
        String attrName,
        String fieldType,
        String column,
        List<String> supportedSymbols,
        List<FieldItem> itemList,
        FieldEntity.BusinessSelectConfig businessSelectConfig
    ) {
    }
}
