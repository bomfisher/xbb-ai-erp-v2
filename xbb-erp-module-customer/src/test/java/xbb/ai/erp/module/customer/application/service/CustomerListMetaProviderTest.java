package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.customer.application.provider.CustomerListMetaProvider;
import xbb.ai.erp.module.customer.application.field.DefaultCustomerFieldFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerListMetaProviderTest {

    @Test
    void should_build_customer_filter_and_default_top_button() {
        ListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        ListMetaBundlePojo topBundle = provider.buildTopButtonMeta(dto);
        ListMetaBundlePojo bottomBundle = provider.buildBottomButtonMeta(dto);
        Map<String, FilterField> filterMap = filterList.stream()
            .collect(Collectors.toMap(FilterField::getAttr, Function.identity()));
        Map<String, xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.CUSTOMER.getCode(), provider.businessCode());
        assertFalse(filterList.isEmpty());
        assertFalse(headerList.isEmpty());
        assertEquals("customerCode", filterList.get(0).getAttr());
        assertEquals("main.customerCode", headerList.get(0).getAttr());
        assertEquals("TEXT", filterMap.get("customerCode").getFieldType());
        assertEquals(List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"),
            filterMap.get("customerCode").getSupportedSymbols());
        assertEquals("DATE", filterMap.get("createTime").getFieldType());
        assertEquals(List.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"),
            filterMap.get("createTime").getSupportedSymbols());
        assertNotNull(filterMap.get("bizStatus").getItemList());
        assertFalse(filterMap.get("bizStatus").getItemList().isEmpty());
        assertEquals("1", String.valueOf(filterMap.get("bizStatus").getItemList().get(0).getValue()));
        assertEquals("启用", filterMap.get("bizStatus").getItemList().get(0).getText());
        assertEquals("0", String.valueOf(filterMap.get("bizStatus").getItemList().get(1).getValue()));
        assertEquals("停用", filterMap.get("bizStatus").getItemList().get(1).getText());
        assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
        assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
        assertEquals("customer_code", conditionMetaMap.get("customerCode").getColumn());
        assertEquals("TEXT", conditionMetaMap.get("customerCode").getFieldType());
    }

    @Test
    void should_build_customer_row_action_meta() {
        CustomerListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

        ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);

        assertEquals(1, rowActionBundle.getRowActionList().size());
        assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
        assertEquals("编辑", rowActionBundle.getRowActionList().get(0).getActionName());
        assertEquals("PRIMARY", rowActionBundle.getRowActionList().get(0).getShowMode());
        assertEquals("NONE", rowActionBundle.getRowActionList().get(0).getConfirmType());
    }

    @Test
    void should_only_expose_supported_customer_list_filters() {
        ListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

        List<String> attrs = provider.buildFilterMeta(dto).stream().map(FilterField::getAttr).toList();

        assertEquals(List.of(
            "customerCode",
            "customerName",
            "customerCategory",
            "regionCode",
            "ownerSalesId",
            "bizStatus",
            "createTime"
        ), attrs);
        assertTrue(attrs.stream().noneMatch(attr -> attr.startsWith("contacts.")));
        assertTrue(attrs.stream().noneMatch(attr -> attr.startsWith("addresses.")));
        assertTrue(attrs.stream().noneMatch(attr -> attr.startsWith("invoiceProfiles.")));
        assertTrue(attrs.stream().noneMatch(attr -> attr.startsWith("main.")));
    }

    @Test
    void should_build_filter_condition_meta_from_same_definition() {
        CustomerListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));

        Map<String, xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo> metaMap = provider.buildConditionMetaMap();

        assertEquals(Set.of(
            "customerCode",
            "customerName",
            "customerCategory",
            "regionCode",
            "ownerSalesId",
            "bizStatus",
            "createTime"
        ), metaMap.keySet());
        assertEquals("customer_code", metaMap.get("customerCode").getColumn());
        assertEquals("TEXT", metaMap.get("customerCode").getFieldType());
        assertEquals(Set.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"),
            metaMap.get("customerCode").getSupportedSymbols());
        assertEquals("add_time", metaMap.get("createTime").getColumn());
        assertEquals("DATE", metaMap.get("createTime").getFieldType());
        assertEquals(Set.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"),
            metaMap.get("createTime").getSupportedSymbols());
    }
}
