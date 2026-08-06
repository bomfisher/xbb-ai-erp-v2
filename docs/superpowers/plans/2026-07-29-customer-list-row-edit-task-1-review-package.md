# Task 1 Review Package

## Scope Files
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- `docs/api/common-list.md`

## Diff Stat
```
 docs/api/common-list.md                            | 56 ++++++++++++++++++++++
 .../module/common/admin/ListCommonController.java  |  6 +++
 .../application/pojo/ListMetaBundlePojo.java       |  2 +
 .../application/provider/ListMetaProvider.java     |  2 +
 .../application/service/ListCommonService.java     |  3 ++
 .../service/impl/ListCommonServiceImpl.java        | 10 ++++
 .../admin/ListCommonControllerStructureTest.java   |  6 +++
 .../application/service/ListCommonServiceTest.java | 33 +++++++++++++
 .../provider/CustomerListMetaProvider.java         | 17 +++++++
 9 files changed, 135 insertions(+)
```

## Unified Diff
```diff
diff --git a/docs/api/common-list.md b/docs/api/common-list.md
index e44ed32..d32bc1d 100644
--- a/docs/api/common-list.md
+++ b/docs/api/common-list.md
@@ -220,10 +220,66 @@
 | `list[].buttonCode` | 是 | 按钮编码 |
 | `list[].buttonName` | 是 | 按钮名称 |
 | `list[].sort` | 是 | 排序值 |
 | `list[].actionCode` | 是 | 动作编码 |
 
 ### 规则说明
 
 - 入参使用 `ListCommonQueryDTO`
 - 响应使用 `ListBottomButtonVO`
 - 结果仅包含底部按钮元数据
+
+## 公共列表行内动作
+
+`POST /erp/v1/common/list/rowAction`
+
+### 请求示例
+
+```json
+{
+  "corpid": "corp-001",
+  "userId": "u-001",
+  "businessCode": "CUSTOMER"
+}
+```
+
+### 参数说明
+
+| 字段 | 是否必填 | 备注 |
+| --- | --- | --- |
+| `corpid` | 是 | 企业 ID |
+| `userId` | 是 | 员工 ID |
+| `businessCode` | 是 | 业务编码 |
+
+### 响应示例
+
+```json
+{
+  "list": [
+    {
+      "actionCode": "EDIT",
+      "actionName": "编辑",
+      "sort": 10,
+      "showMode": "PRIMARY",
+      "confirmType": null
+    }
+  ]
+}
+```
+
+### 响应参数说明
+
+| 字段 | 是否必返 | 备注 |
+| --- | --- | --- |
+| `list` | 是 | 行内动作列表 |
+| `list[].actionCode` | 是 | 动作编码 |
+| `list[].actionName` | 是 | 动作名称 |
+| `list[].sort` | 是 | 排序值 |
+| `list[].showMode` | 是 | 展示模式，当前客户列表首版返回 `PRIMARY` |
+| `list[].confirmType` | 否 | 二次确认类型；当前未配置时返回 `null` |
+
+### 规则说明
+
+- 入参使用 `ListCommonQueryDTO`
+- 响应使用 `ListRowActionVO`
+- 结果仅包含行内动作元数据
+- 本次仅落地客户列表 `EDIT` 动作，不提前暴露其他动作
diff --git a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java
index bb52c0a..dae9f4b 100644
--- a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java
+++ b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java
@@ -3,20 +3,21 @@ package xbb.ai.erp.module.common.admin;
 import lombok.RequiredArgsConstructor;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
 import xbb.ai.erp.base.common.vo.ResultVO;
 import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
 import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
 import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
 import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
+import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
 import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
 import xbb.ai.erp.module.common.application.service.ListCommonService;
 
 @RestController
 @RequestMapping("/erp/v1/common/list")
 @RequiredArgsConstructor
 public class ListCommonController {
 
     private final ListCommonService listCommonService;
 
@@ -32,11 +33,16 @@ public class ListCommonController {
 
     @PostMapping("/topButton")
     public ResultVO<ListTopButtonVO> topButton(@RequestBody ListCommonQueryDTO dto) {
         return ResultVO.success(listCommonService.topButton(dto));
     }
 
     @PostMapping("/bottomButton")
     public ResultVO<ListBottomButtonVO> bottomButton(@RequestBody ListCommonQueryDTO dto) {
         return ResultVO.success(listCommonService.bottomButton(dto));
     }
+
+    @PostMapping("/rowAction")
+    public ResultVO<ListRowActionVO> rowAction(@RequestBody ListCommonQueryDTO dto) {
+        return ResultVO.success(listCommonService.rowAction(dto));
+    }
 }
diff --git a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java
index d2cda9e..7ba1d8b 100644
--- a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java
+++ b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java
@@ -1,14 +1,16 @@
 package xbb.ai.erp.module.common.application.pojo;
 
 import lombok.Data;
 import xbb.ai.erp.module.common.admin.pojo.FilterField;
 import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
+import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
 
 import java.util.List;
 
 @Data
 public class ListMetaBundlePojo {
     private List<FilterField> filterList;
     private List<ListButtonItemPojo> topButtonList;
     private List<ListButtonItemPojo> bottomButtonList;
+    private List<ListRowActionItemPojo> rowActionList;
 }
diff --git a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java
index 82c0ade..80e79be 100644
--- a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java
+++ b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java
@@ -13,16 +13,18 @@ public interface ListMetaProvider {
     String businessCode();
 
     List<FilterField> buildFilterMeta(ListCommonQueryDTO dto);
 
     List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto);
 
     ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto);
 
     ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto);
 
+    ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto);
+
     default void applyPackageExtension(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
     }
 
     default void applyPermissionTrim(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
     }
 }
diff --git a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java
index 3dc8725..5071430 100644
--- a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java
+++ b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java
@@ -1,18 +1,21 @@
 package xbb.ai.erp.module.common.application.service;
 
 import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
 import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
 import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
 import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
+import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
 import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
 
 public interface ListCommonService {
 
     ListFilterVO filter(ListCommonQueryDTO dto);
 
     ListHeaderVO header(ListCommonQueryDTO dto);
 
     ListTopButtonVO topButton(ListCommonQueryDTO dto);
 
     ListBottomButtonVO bottomButton(ListCommonQueryDTO dto);
+
+    ListRowActionVO rowAction(ListCommonQueryDTO dto);
 }
diff --git a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java
index 07ecb38..ec272e6 100644
--- a/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java
+++ b/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java
@@ -1,18 +1,19 @@
 package xbb.ai.erp.module.common.application.service.impl;
 
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;
 import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
 import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
 import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
 import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
+import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
 import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
 import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
 import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
 import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
 import xbb.ai.erp.module.common.application.service.ListCommonService;
 
 @Service
 @RequiredArgsConstructor
 public class ListCommonServiceImpl implements ListCommonService {
 
@@ -44,11 +45,20 @@ public class ListCommonServiceImpl implements ListCommonService {
     }
 
     @Override
     public ListBottomButtonVO bottomButton(ListCommonQueryDTO dto) {
         ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
         ListMetaBundlePojo bundle = provider.buildBottomButtonMeta(dto);
         ListBottomButtonVO vo = new ListBottomButtonVO();
         vo.setList(bundle.getBottomButtonList());
         return vo;
     }
+
+    @Override
+    public ListRowActionVO rowAction(ListCommonQueryDTO dto) {
+        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
+        ListMetaBundlePojo bundle = provider.buildRowActionMeta(dto);
+        ListRowActionVO vo = new ListRowActionVO();
+        vo.setList(bundle.getRowActionList());
+        return vo;
+    }
 }
diff --git a/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java b/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java
index 92da679..35a8f58 100644
--- a/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java
+++ b/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java
@@ -32,11 +32,17 @@ class ListCommonControllerStructureTest {
         Class<?> bottomButtonVoClass = Class.forName("xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO");
         Class<?> buttonItemClass = Class.forName("xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo");
 
         Field topList = topButtonVoClass.getDeclaredField("list");
         Field bottomList = bottomButtonVoClass.getDeclaredField("list");
 
         assertEquals(List.class, topList.getType());
         assertEquals(List.class, bottomList.getType());
         assertNotNull(buttonItemClass);
     }
+
+    @Test
+    void should_define_row_action_endpoint_on_common_controller() throws Exception {
+        Method rowAction = ListCommonController.class.getMethod("rowAction", Class.forName("xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO"));
+        assertNotNull(rowAction);
+    }
 }
diff --git a/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java b/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java
index c1ddcce..87ff316 100644
--- a/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java
+++ b/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java
@@ -1,23 +1,25 @@
 package xbb.ai.erp.module.common.application.service;
 
 import org.junit.jupiter.api.Test;
 import xbb.ai.erp.base.common.exception.BizException;
 import xbb.ai.erp.base.common.filed.FieldEntity;
 import xbb.ai.erp.base.common.filed.FieldItem;
 import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
 import xbb.ai.erp.module.common.admin.pojo.FilterField;
 import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
 import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
+import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
 import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
 import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
 import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
+import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
 import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
 import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
 import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
 import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
 import xbb.ai.erp.module.common.application.service.impl.ListCommonServiceImpl;
 
 import java.util.List;
 
 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertThrows;
@@ -49,25 +51,43 @@ class ListCommonServiceTest {
         assertEquals("导出", bottomButtonVO.getList().get(0).getButtonName());
     }
 
     @Test
     void should_define_independent_provider_methods_for_three_endpoints() {
         try {
             ListMetaProvider.class.getMethod("buildFilterMeta", ListCommonQueryDTO.class);
             ListMetaProvider.class.getMethod("buildHeaderMeta", ListCommonQueryDTO.class);
             ListMetaProvider.class.getMethod("buildTopButtonMeta", ListCommonQueryDTO.class);
             ListMetaProvider.class.getMethod("buildBottomButtonMeta", ListCommonQueryDTO.class);
+            ListMetaProvider.class.getMethod("buildRowActionMeta", ListCommonQueryDTO.class);
         } catch (NoSuchMethodException exception) {
             fail(exception);
         }
     }
 
+    @Test
+    void should_dispatch_row_action_meta_by_business_code() {
+        ListMetaProvider provider = new StubListMetaProvider();
+        ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
+        ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
+        ListCommonQueryDTO dto = new ListCommonQueryDTO();
+        dto.setBusinessCode("CUSTOMER");
+        dto.setCorpid("corp-001");
+        dto.setUserId("user-001");
+
+        ListRowActionVO rowActionVO = service.rowAction(dto);
+
+        assertEquals("EDIT", rowActionVO.getList().get(0).getActionCode());
+        assertEquals("编辑", rowActionVO.getList().get(0).getActionName());
+        assertEquals("PRIMARY", rowActionVO.getList().get(0).getShowMode());
+    }
+
     @Test
     void should_fail_when_business_code_is_not_registered() {
         ListCommonServiceImpl service = new ListCommonServiceImpl(new ListMetaRegistry(List.of()));
         ListCommonQueryDTO dto = new ListCommonQueryDTO();
         dto.setBusinessCode("UNKNOWN");
 
         BizException exception = assertThrows(BizException.class, () -> service.filter(dto));
         assertEquals("未找到业务编码对应的列表元数据提供者: UNKNOWN", exception.getMessage());
     }
 
@@ -133,12 +153,25 @@ class ListCommonServiceTest {
             ListButtonItemPojo bottomButton = new ListButtonItemPojo();
             bottomButton.setButtonCode("EXPORT");
             bottomButton.setButtonName("导出");
             bottomButton.setSort(20);
             bottomButton.setActionCode("EXPORT");
 
             ListMetaBundlePojo bundle = new ListMetaBundlePojo();
             bundle.setBottomButtonList(List.of(bottomButton));
             return bundle;
         }
+
+        @Override
+        public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
+            ListRowActionItemPojo rowAction = new ListRowActionItemPojo();
+            rowAction.setActionCode("EDIT");
+            rowAction.setActionName("编辑");
+            rowAction.setSort(10);
+            rowAction.setShowMode("PRIMARY");
+
+            ListMetaBundlePojo bundle = new ListMetaBundlePojo();
+            bundle.setRowActionList(List.of(rowAction));
+            return bundle;
+        }
     }
 }
diff --git a/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java b/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java
index bd2d5f1..71992b0 100644
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
@@ -89,39 +90,55 @@ public class CustomerListMetaProvider implements ListMetaProvider {
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
+        bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY")));
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
 
+    private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode) {
+        ListRowActionItemPojo item = new ListRowActionItemPojo();
+        item.setActionCode(actionCode);
+        item.setActionName(actionName);
+        item.setSort(sort);
+        item.setShowMode(showMode);
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
```
