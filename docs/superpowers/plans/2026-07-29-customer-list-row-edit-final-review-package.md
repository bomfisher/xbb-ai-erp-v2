# Customer List Row Edit Final Review Package

## Scope
Only review the files related to the customer list row edit implementation from this session.
Ignore unrelated existing branch/worktree changes in purchase modules and other docs.

## Files in scope
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java
- /Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.vue
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.spec.ts
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.vue
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.spec.ts
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/customerCreate.ts
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerCreateDrawer.vue
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue
- /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts
- /Users/bomfish/xbb-ai-erp-v2/docs/api/common-list.md
- /Users/bomfish/xbb-ai-erp-v2/docs/api/customer-customer.md

## Plan checklist
- 后端新增 /erp/v1/common/list/rowAction 协议
- 客户列表后端下发 EDIT 行动作
- 前端新增 ListRowActions 通用组件
- ListDataTable 增加右侧固定操作列
- CustomerCreateDrawer 支持 create/edit 双模式
- CustomerListPage 接通 rowAction 与 EDIT 编排
- docs/api 已与当前代码一致

## Verification already reported
- backend common tests passed
- backend customer provider tests passed
- frontend row action / data table / customer list specs passed
