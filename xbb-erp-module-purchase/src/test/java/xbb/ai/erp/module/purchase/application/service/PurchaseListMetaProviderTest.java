package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchaseOrderItemListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchaseOrderListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchasePendingTaskListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchaseRequestItemListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchaseRequestListMetaProvider;
import xbb.ai.erp.module.purchase.application.provider.PurchaseSourceRelationListMetaProvider;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseListMetaProviderTest {

    @Test
    void should_build_purchase_request_list_meta() {
        ListMetaProvider provider = new PurchaseRequestListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_REQUEST);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        ListMetaBundlePojo topBundle = provider.buildTopButtonMeta(dto);
        ListMetaBundlePojo bottomBundle = provider.buildBottomButtonMeta(dto);
        ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PURCHASE_REQUEST.getCode(), provider.businessCode());
        assertEquals(List.of("requestNo", "purchaseOrgId", "requestDeptId", "applicantId", "sourceType", "sourceNo", "suggestedVendorId", "suggestedDeliveryDate", "bizStatus", "approvalStatus"),
            filterList.stream().map(FilterField::getAttr).toList());
        assertEquals(List.of("main.requestNo", "main.purchaseOrgId", "main.sourceType", "main.bizStatus", "main.approvalStatus", "main.grossAmount", "main.netAmount", "main.taxAmount", "main.addTime", "main.updateTime"),
            headerList.stream().map(FieldEntity::getAttr).toList());
        assertEquals("request_no", conditionMetaMap.get("requestNo").getColumn());
        assertEquals("DATE", conditionMetaMap.get("suggestedDeliveryDate").getFieldType());
        assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
        assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
        assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
    }

    @Test
    void should_build_purchase_order_list_meta() {
        ListMetaProvider provider = new PurchaseOrderListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_ORDER);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PURCHASE_ORDER.getCode(), provider.businessCode());
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("orderNo")));
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("receiptStatus")));
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("inboundStatus")));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.vendorNameSnapshot"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.receiptStatus"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.inboundStatus"));
        assertEquals("order_no", conditionMetaMap.get("orderNo").getColumn());
        assertEquals("TEXT", conditionMetaMap.get("receiptStatus").getFieldType());
        assertEquals("TEXT", conditionMetaMap.get("inboundStatus").getFieldType());
    }

    @Test
    void should_build_purchase_pending_task_list_meta() {
        ListMetaProvider provider = new PurchasePendingTaskListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_PENDING_TASK);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PURCHASE_PENDING_TASK.getCode(), provider.businessCode());
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("taskNo")));
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("taskStatus")));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.generatedRequestQty"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.generatedOrderQty"));
        assertEquals("task_no", conditionMetaMap.get("taskNo").getColumn());
        assertEquals("task_status", conditionMetaMap.get("taskStatus").getColumn());
    }

    @Test
    void should_build_purchase_source_relation_list_meta() {
        ListMetaProvider provider = new PurchaseSourceRelationListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_SOURCE_RELATION);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PURCHASE_SOURCE_RELATION.getCode(), provider.businessCode());
        assertEquals(List.of("sourceDocType", "sourceDocId", "sourceLineId", "targetDocType", "targetDocId", "targetLineId", "relationStatus"),
            filterList.stream().map(FilterField::getAttr).toList());
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.sourceQty"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.reversedQty"));
        assertEquals("relation_status", conditionMetaMap.get("relationStatus").getColumn());
    }

    @Test
    void should_build_purchase_request_item_list_meta() {
        ListMetaProvider provider = new PurchaseRequestItemListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_REQUEST_ITEM);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PURCHASE_REQUEST_ITEM.getCode(), provider.businessCode());
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("requestId")));
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("skuCodeSnapshot")));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.requestQty"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.executedQty"));
        assertEquals("request_id", conditionMetaMap.get("requestId").getColumn());
    }

    @Test
    void should_build_purchase_order_item_list_meta() {
        ListMetaProvider provider = new PurchaseOrderItemListMetaProvider();
        ListCommonQueryDTO dto = buildDto(BusinessCodeEnum.PURCHASE_ORDER_ITEM);

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        Map<String, ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);
        FilterField giftFilter = filterList.stream().filter(field -> field.getAttr().equals("isGift")).findFirst().orElseThrow();

        assertEquals(BusinessCodeEnum.PURCHASE_ORDER_ITEM.getCode(), provider.businessCode());
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("orderId")));
        assertTrue(filterList.stream().anyMatch(field -> field.getAttr().equals("isGift")));
        assertEquals("ENUM", giftFilter.getFieldType());
        assertNotNull(giftFilter.getItemList());
        assertFalse(giftFilter.getItemList().isEmpty());
        assertEquals("0", String.valueOf(giftFilter.getItemList().get(0).getValue()));
        assertEquals("否", giftFilter.getItemList().get(0).getText());
        assertEquals("1", String.valueOf(giftFilter.getItemList().get(1).getValue()));
        assertEquals("是", giftFilter.getItemList().get(1).getText());
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.orderQty"));
        assertTrue(headerList.stream().map(FieldEntity::getAttr).toList().contains("main.inboundedQty"));
        assertEquals("is_gift", conditionMetaMap.get("isGift").getColumn());
        assertEquals("ENUM", conditionMetaMap.get("isGift").getFieldType());
    }

    private static ListCommonQueryDTO buildDto(BusinessCodeEnum businessCodeEnum) {
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(businessCodeEnum.getCode());
        return dto;
    }
}
