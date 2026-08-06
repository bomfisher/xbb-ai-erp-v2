package xbb.ai.erp.module.supplier.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.supplier.application.provider.SupplierListMetaProvider;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SupplierListMetaProviderTest {

    @Test
    void should_build_supplier_list_meta() {
        ListMetaProvider provider = new SupplierListMetaProvider();
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.SUPPLIER.getCode());

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        ListMetaBundlePojo topBundle = provider.buildTopButtonMeta(dto);
        ListMetaBundlePojo bottomBundle = provider.buildBottomButtonMeta(dto);
        ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);
        Map<String, xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.SUPPLIER.getCode(), provider.businessCode());
        assertFalse(filterList.isEmpty());
        assertEquals(List.of("supplierCode", "supplierName", "supplierCategory", "mainBusinessCategory", "ownerPurchaserId", "bizStatus", "refStatus"),
            filterList.stream().map(FilterField::getAttr).toList());
        assertEquals(List.of(
            "main.supplierCode",
            "main.supplierName",
            "main.supplierShortName",
            "main.supplierCategory",
            "main.mainBusinessCategory",
            "main.ownerPurchaserId",
            "main.bizStatus",
            "main.refStatus",
            "main.addTime",
            "main.updateTime"
        ), headerList.stream().map(FieldEntity::getAttr).toList());
        assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
        assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
        assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
        assertEquals("supplier_code", conditionMetaMap.get("supplierCode").getColumn());
        assertEquals("TEXT", conditionMetaMap.get("supplierCode").getFieldType());
        assertEquals("owner_purchaser_id", conditionMetaMap.get("ownerPurchaserId").getColumn());
        FilterField supplierCategory = filterList.stream().filter(item -> "supplierCategory".equals(item.getAttr())).findFirst().orElseThrow();
        FilterField ownerPurchaser = filterList.stream().filter(item -> "ownerPurchaserId".equals(item.getAttr())).findFirst().orElseThrow();
        FieldEntity supplierCategoryHeader = headerList.stream().filter(item -> "main.supplierCategory".equals(item.getAttr())).findFirst().orElseThrow();
        FieldEntity ownerPurchaserHeader = headerList.stream().filter(item -> "main.ownerPurchaserId".equals(item.getAttr())).findFirst().orElseThrow();
        assertEquals("ENUM", supplierCategory.getFieldType());
        assertFalse(supplierCategory.getItemList().isEmpty());
        assertEquals("USER", ownerPurchaser.getFieldType());
        assertEquals("/erp/v1/org/memberSelect/quickSearch", ownerPurchaser.getBusinessSelectConfig().getQuickSearchUrl());
        assertEquals(String.valueOf(xbb.ai.erp.base.common.filed.FieldTypeEnum.COMB.getType()), supplierCategoryHeader.getFieldType());
        assertEquals(String.valueOf(xbb.ai.erp.base.common.filed.FieldTypeEnum.USER.getType()), ownerPurchaserHeader.getFieldType());
        assertEquals("/erp/v1/org/memberSelect/getById", ownerPurchaserHeader.getBusinessSelectConfig().getGetByIdUrl());
    }
}
