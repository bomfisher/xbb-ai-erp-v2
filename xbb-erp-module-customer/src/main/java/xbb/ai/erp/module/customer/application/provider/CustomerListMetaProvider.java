package xbb.ai.erp.module.customer.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.base.common.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.customer.admin.CustomerBizStatusEnum;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CustomerListMetaProvider implements ListMetaProvider {

    private static final List<String> TEXT_SYMBOLS = List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> ENUM_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> ID_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> DATE_SYMBOLS = List.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<FieldItem> BIZ_STATUS_ITEMS = CustomerBizStatusEnum.toFieldItems();
    private static final List<CustomerListFilterDefinition> FILTER_DEFINITIONS = List.of(
        new CustomerListFilterDefinition("customerCode", "客户编码", "TEXT", "customer_code", TEXT_SYMBOLS, List.of()),
        new CustomerListFilterDefinition("customerName", "客户名称", "TEXT", "customer_name", TEXT_SYMBOLS, List.of()),
        new CustomerListFilterDefinition("customerCategory", "客户分类", "ENUM", "customer_category", ENUM_SYMBOLS, List.of()),
        new CustomerListFilterDefinition("regionCode", "所属区域", "ENUM", "region_code", ENUM_SYMBOLS, List.of()),
        new CustomerListFilterDefinition("ownerSalesId", "归属销售", "ID", "owner_sales_id", ID_SYMBOLS, List.of()),
        new CustomerListFilterDefinition("bizStatus", "业务状态", "ENUM", "biz_status", ENUM_SYMBOLS, BIZ_STATUS_ITEMS),
        new CustomerListFilterDefinition("createTime", "创建时间", "DATE", "add_time", DATE_SYMBOLS, List.of())
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = buildConditionMetaMap(FILTER_DEFINITIONS);

    private final CustomerFieldFactory customerFieldFactory;

    public CustomerListMetaProvider(CustomerFieldFactory customerFieldFactory) {
        this.customerFieldFactory = customerFieldFactory;
    }

    @Override
    public String businessCode() {
        return BusinessCodeEnum.CUSTOMER.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return FILTER_DEFINITIONS.stream().map(CustomerListMetaProvider::buildFilterField).toList();
    }

    public static Map<String, ListFilterMetaPojo> conditionMetaMap() {
        return CONDITION_META_MAP;
    }

    public static Map<String, ListFilterMetaPojo> buildConditionMetaMap() {
        return conditionMetaMap();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return conditionMetaMap();
    }

    private static Map<String, ListFilterMetaPojo> buildConditionMetaMap(List<CustomerListFilterDefinition> definitions) {
        Map<String, ListFilterMetaPojo> metaMap = new LinkedHashMap<>();
        for (CustomerListFilterDefinition definition : definitions) {
            metaMap.put(definition.attr(), new ListFilterMetaPojo(
                definition.attr(),
                definition.column(),
                definition.fieldType(),
                    definition.supportedSymbols()
            ));
        }
        return Collections.unmodifiableMap(metaMap);
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return CustomerFieldAssembler.buildHeadList(customerFieldFactory.getFields(SceneTypeEnum.LIST));
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

    private static FilterField buildFilterField(CustomerListFilterDefinition definition) {
        FilterField field = new FilterField();
        field.setAttr(definition.attr());
        field.setAttrName(definition.attrName());
        field.setFieldType(definition.fieldType());
        field.setSupportedSymbols(definition.supportedSymbols());
        field.setItemList(definition.itemList());
        return field;
    }

    private ListButtonItemPojo buildButton(String buttonCode, String buttonName, Integer sort, String actionCode) {
        ListButtonItemPojo item = new ListButtonItemPojo();
        item.setButtonCode(buttonCode);
        item.setButtonName(buttonName);
        item.setSort(sort);
        item.setActionCode(actionCode);
        return item;
    }

    private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
        ListRowActionItemPojo item = new ListRowActionItemPojo();
        item.setActionCode(actionCode);
        item.setActionName(actionName);
        item.setSort(sort);
        item.setShowMode(showMode);
        item.setConfirmType(confirmType);
        return item;
    }

    private record CustomerListFilterDefinition(
        String attr,
        String attrName,
        String fieldType,
        String column,
        List<String> supportedSymbols,
        List<FieldItem> itemList
    ) {
    }
}
