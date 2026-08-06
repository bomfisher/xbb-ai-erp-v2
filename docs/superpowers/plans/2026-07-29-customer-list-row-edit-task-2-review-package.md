# Task 2 Review Package

## Scope Files
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`

## Diff Stat
```
 .../application/provider/CustomerListMetaProvider.java | 18 ++++++++++++++++++
 .../service/CustomerListMetaProviderTest.java          | 18 +++++++++++++++++-
 2 files changed, 35 insertions(+), 1 deletion(-)
```

## Unified Diff
```diff
diff --git a/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java b/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java
index bd2d5f1..6789c35 100644
--- a/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java
+++ b/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java
@@ -1,19 +1,20 @@
 package xbb.ai.erp.module.customer.application.provider;
 
 import org.springframework.stereotype.Component;
 import xbb.ai.erp.base.common.filed.FieldEntity;
 import xbb.ai.erp.base.common.filed.FieldItem;
 import xbb.ai.erp.base.common.module.BusinessCodeEnum;
 import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
 import xbb.ai.erp.module.common.admin.pojo.FilterField;
 import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
+import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
 import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
 import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
 import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
 import xbb.ai.erp.module.customer.admin.CustomerBizStatusEnum;
 import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
 import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
 import xbb.ai.erp.scene.meta.SceneTypeEnum;
 
 import java.util.Collections;
 import java.util.LinkedHashMap;
@@ -89,39 +90,56 @@ public class CustomerListMetaProvider implements ListMetaProvider {
         return bundle;
     }
 
     @Override
     public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
         ListMetaBundlePojo bundle = new ListMetaBundlePojo();
         bundle.setBottomButtonList(List.of(buildButton("EXPORT", "导出", 20, "EXPORT")));
         return bundle;
     }
 
+    @Override
+    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
+        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
+        bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
+        return bundle;
+    }
+
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
 
+    private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
+        ListRowActionItemPojo item = new ListRowActionItemPojo();
+        item.setActionCode(actionCode);
+        item.setActionName(actionName);
+        item.setSort(sort);
+        item.setShowMode(showMode);
+        item.setConfirmType(confirmType);
+        return item;
+    }
+
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
diff --git a/xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java b/xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java
index 427ff63..da66b73 100644
--- a/xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java
+++ b/xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java
@@ -13,21 +13,20 @@ import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.function.Function;
 import java.util.stream.Collectors;
 
 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertFalse;
 import static org.junit.jupiter.api.Assertions.assertNotNull;
 import static org.junit.jupiter.api.Assertions.assertTrue;
-import static org.junit.jupiter.api.Assertions.assertEquals;
 
 class CustomerListMetaProviderTest {
 
     @Test
     void should_build_customer_filter_and_default_top_button() {
         ListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
         ListCommonQueryDTO dto = new ListCommonQueryDTO();
         dto.setCorpid("corp-001");
         dto.setUserId("user-001");
         dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());
@@ -53,20 +52,37 @@ class CustomerListMetaProviderTest {
         assertNotNull(filterMap.get("bizStatus").getItemList());
         assertFalse(filterMap.get("bizStatus").getItemList().isEmpty());
         assertEquals("1", String.valueOf(filterMap.get("bizStatus").getItemList().get(0).getValue()));
         assertEquals("启用", filterMap.get("bizStatus").getItemList().get(0).getText());
         assertEquals("0", String.valueOf(filterMap.get("bizStatus").getItemList().get(1).getValue()));
         assertEquals("停用", filterMap.get("bizStatus").getItemList().get(1).getText());
         assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
         assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
     }
 
+    @Test
+    void should_build_customer_row_action_meta() {
+        CustomerListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
+        ListCommonQueryDTO dto = new ListCommonQueryDTO();
+        dto.setCorpid("corp-001");
+        dto.setUserId("user-001");
+        dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());
+
+        ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);
+
+        assertEquals(1, rowActionBundle.getRowActionList().size());
+        assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
+        assertEquals("编辑", rowActionBundle.getRowActionList().get(0).getActionName());
+        assertEquals("PRIMARY", rowActionBundle.getRowActionList().get(0).getShowMode());
+        assertEquals("NONE", rowActionBundle.getRowActionList().get(0).getConfirmType());
+    }
+
     @Test
     void should_only_expose_supported_customer_list_filters() {
         ListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
         ListCommonQueryDTO dto = new ListCommonQueryDTO();
         dto.setCorpid("corp-001");
         dto.setUserId("user-001");
         dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());
 
         List<String> attrs = provider.buildFilterMeta(dto).stream().map(FilterField::getAttr).toList();
```
