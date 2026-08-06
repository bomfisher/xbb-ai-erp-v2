# module-purchase 按 module-customer 规范全量镜像改造 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 `xbb-erp-module-purchase` 后端协议、DDD 分层和前端页面重构为与 `xbb-erp-module-customer` 当前实现对齐的最终态。

**Architecture:** 后端采用“协议统一、领域分治”方式：每个采购子模块对外表现为 `customer` 风格标准管理模块，对内保留独立领域对象、仓储与持久化实现。前端以 `customer` 的列表、详情、新建编辑、草稿、提交交互为模板，为 `purchase` 全量落地同类页面和路由。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、Spring Web、MyBatis-Plus、JUnit 5、Vue 3、前端 admin-web

## Global Constraints

- 项目架构遵循 DDD，目录与职责拆分参考 `module-customer` 当前实现。
- 对话与注释保持中文语境。
- 所有主动捕获的报错、业务主动抛错统一使用 `BizException`。
- 直连数据库对象统一使用 `PO` 后缀，字段不使用布尔值，统一使用 `Integer` 等显式类型。
- 枚举类统一以 `Enum` 结尾。
- 入参统一使用 DTO，非脚本接口 DTO 继承 `BaseDTO` 或现有基础 DTO 体系。
- 无业务返回统一使用 `BaseVO`，所有接口统一使用 `ResultVO.success()` 包装。
- `userId` / 员工 ID 为字符串。
- 尽可能避免循环中查询数据库。
- 以 `xbb-erp-module-customer` 当前代码实现为最高对齐基准，允许破坏 `purchase` 现有接口兼容性。
- 前端仓库位于 `/Users/bomfish/xbb-ai-erp-v2-front`。
- 尽量不改表；只有现有表无法承载草稿、状态与详情协议时，才补最小化 SQL。
- 实现完成后执行 `gen-api-md` 同步接口文档。

---

### Task 1: 梳理 customer 模板并定义 purchase 统一协议

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/**/*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/**/*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/domain/**/*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/**/*.java`
- Test: `xbb-erp-module-purchase/src/test/java/**/*.java`

**Interfaces:**
- Consumes: `CustomerAdminController`、`CustomerAdminAppService`、`CustomerQueryAppService`、`CustomerSaveAppService`、`CustomerDraftAppService`、`CustomerDeleteAppService`
- Produces: 采购各子模块统一的 `list / addItem / updateItem / saveDraft / draftList / loadDraft / saveAndSubmit / detail / delete` 协议清单与类清单

- [ ] **Step 1: 明确 customer 模板映射表**

```text
PurchaseRequest       -> Customer
PurchaseRequestItem   -> CustomerContact/子档模式
PurchaseOrder         -> Customer 主档模式
PurchaseOrderItem     -> Customer 子档模式
PurchasePendingTask   -> Customer 查询协议的只读子集
PurchaseSourceRelation-> Customer 查询/详情协议的轻量子集
```

- [ ] **Step 2: 列出 purchase 现有不一致点并确定统一修正方向**

```text
1. list DTO 从 BaseDTO 调整为 ListBaseDTO 风格
2. save 返回从 ResultVO<Long> 调整为 ResultVO<*SaveItemVO>
3. delete 返回从 ResultVO<Void> 调整为 ResultVO<BaseVO>
4. controller 只保留协议接入，业务逻辑下沉到 application 子服务
5. 可编辑子模块补齐草稿与提交链路；查询子模块至少补齐 list/detail 统一协议
```

- [ ] **Step 3: 运行受影响文件列表检查**

Run: `rg -n "ResultVO<Long>|ResultVO<Void>|extends BaseDTO|AdminAppServiceImpl" xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase`
Expected: 输出当前所有待对齐位置，作为后续实施清单

- [ ] **Step 4: 提交阶段性变更**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: define purchase alignment targets"
```

### Task 2: 重构 PurchaseRequest 后端为首个完整样板

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/dto/PurchaseRequest*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/vo/PurchaseRequest*.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/query/PurchaseRequestQueryAppService.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/query/PurchaseRequestQueryAppServiceImpl.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/save/PurchaseRequestSaveAppService.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/save/PurchaseRequestSaveAppServiceImpl.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/draft/PurchaseRequestDraftAppService.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/draft/PurchaseRequestDraftAppServiceImpl.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/delete/PurchaseRequestDeleteAppService.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/delete/PurchaseRequestDeleteAppServiceImpl.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/pojo/PurchaseRequest*.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/port/PurchaseRequestDraftRepository.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/validator/PurchaseRequest*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/PurchaseRequestRepositoryImpl.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/PurchaseRequestDraftRepositoryImpl.java`
- Test: `xbb-erp-module-purchase/src/test/java/**/PurchaseRequest*.java`

**Interfaces:**
- Consumes: `PurchaseRequestRepository`, `PurchaseRequestItemRepository`, `PurchaseRequestAdminAssembler`
- Produces: `PurchaseRequestAdminAppService` 标准门面；`PurchaseRequestQueryAppService`、`PurchaseRequestSaveAppService`、`PurchaseRequestDraftAppService`、`PurchaseRequestDeleteAppService`

- [ ] **Step 1: 写首个失败的协议测试**

```java
@Test
void shouldReturnBaseVoWhenDeletePurchaseRequest() {
    ResultVO<BaseVO> result = controller.delete(new IdBaseDTO("PR-1"));
    assertEquals("0", result.getCode());
    assertNotNull(result.getData());
}
```

- [ ] **Step 2: 运行单测确认失败**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchaseRequest*Test test`
Expected: FAIL，提示返回类型或新服务不存在

- [ ] **Step 3: 以 customer 为模板补齐查询、保存、草稿、删除子服务**

```java
public interface PurchaseRequestQueryAppService {
    PageResult<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto);
    PurchaseRequestSaveItemVO addItem();
    PurchaseRequestSaveItemVO updateItem(IdBaseDTO dto);
    PurchaseRequestDetailVO detail(IdBaseDTO dto);
}
```

- [ ] **Step 4: 调整 controller 与 DTO/VO 协议**

```java
@PostMapping("/save-and-submit")
public ResultVO<BaseVO> saveAndSubmit(@RequestBody PurchaseRequestSubmitSaveDTO dto) {
    purchaseRequestAdminAppService.saveAndSubmit(dto);
    return ResultVO.success(BaseVO.success());
}
```

- [ ] **Step 5: 运行模块测试**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchaseRequest*Test test`
Expected: PASS

- [ ] **Step 6: 提交阶段性变更**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: align purchase request admin flow"
```

### Task 3: 将样板复制到 PurchaseOrder 与子档对象

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseOrderAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseOrderItemAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/dto/PurchaseOrder*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/vo/PurchaseOrder*.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/{query,save,draft,delete}/PurchaseOrder*.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/pojo/PurchaseOrder*.java`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/validator/PurchaseOrder*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseOrderAdminAppService.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderAdminAppServiceImpl.java`
- Test: `xbb-erp-module-purchase/src/test/java/**/PurchaseOrder*.java`

**Interfaces:**
- Consumes: Task 2 中沉淀的样板接口与分层方式
- Produces: `PurchaseOrder` 与 `PurchaseOrderItem` 对齐后的标准后端链路

- [ ] **Step 1: 写失败的列表与保存协议测试**

```java
@Test
void shouldUseListBaseDtoForPurchaseOrderList() {
    assertTrue(ListBaseDTO.class.isAssignableFrom(PurchaseOrderListDTO.class));
}
```

- [ ] **Step 2: 运行单测确认失败**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchaseOrder*Test test`
Expected: FAIL，提示 DTO 或协议未对齐

- [ ] **Step 3: 复制 PurchaseRequest 模式到 PurchaseOrder 主档与子档**

```java
public interface PurchaseOrderSaveAppService {
    PurchaseOrderSaveItemVO save(PurchaseOrderSaveDTO dto);
    void saveAndSubmit(PurchaseOrderSubmitSaveDTO dto);
}
```

- [ ] **Step 4: 跑 PurchaseOrder 相关测试**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchaseOrder*Test test`
Expected: PASS

- [ ] **Step 5: 提交阶段性变更**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: align purchase order admin flow"
```

### Task 4: 收敛查询型子模块与其余采购子模块协议

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchasePendingTaskAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseSourceRelationAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestItemAdminController.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/dto/PurchasePendingTask*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/dto/PurchaseSourceRelation*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/vo/PurchasePendingTask*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/vo/PurchaseSourceRelation*.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/**/*.java`
- Test: `xbb-erp-module-purchase/src/test/java/**/PurchasePendingTask*.java`
- Test: `xbb-erp-module-purchase/src/test/java/**/PurchaseSourceRelation*.java`

**Interfaces:**
- Consumes: Task 2 / Task 3 中的统一 controller / app service 模式
- Produces: 查询型模块的统一 `list/detail` 协议；其余子模块的统一返回包装与 DTO 基类

- [ ] **Step 1: 写失败的查询型协议测试**

```java
@Test
void shouldWrapPurchasePendingTaskDetailWithResultVo() {
    Method method = PurchasePendingTaskAdminController.class.getMethod("detail", IdBaseDTO.class);
    assertEquals(ResultVO.class, method.getReturnType());
}
```

- [ ] **Step 2: 运行单测确认失败**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchasePendingTask*Test,PurchaseSourceRelation*Test test`
Expected: FAIL

- [ ] **Step 3: 收敛查询型子模块到统一 list/detail 协议**

```java
public interface PurchasePendingTaskAdminAppService {
    PageResult<PurchasePendingTaskListItemVO> list(PurchasePendingTaskListDTO dto);
    PurchasePendingTaskDetailVO detail(IdBaseDTO dto);
}
```

- [ ] **Step 4: 跑查询型子模块测试**

Run: `mvn -pl xbb-erp-module-purchase -Dtest=PurchasePendingTask*Test,PurchaseSourceRelation*Test test`
Expected: PASS

- [ ] **Step 5: 提交阶段性变更**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: align purchase query modules"
```

### Task 5: 补最小数据库与草稿持久化支撑

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po/*.java`
- Modify: `xbb-erp-module-purchase/src/main/resources/mapper/purchase/*.xml`
- Modify: `docs/sql/**/*.sql`
- Modify: `docs/api/purchase-*.md`

**Interfaces:**
- Consumes: Task 2 / Task 3 的草稿与状态协议
- Produces: 最小化草稿、状态、详情字段支撑和对应 SQL

- [ ] **Step 1: 写失败的持久化映射测试或检查**

```java
@Test
void shouldNotUseBooleanFieldsInPurchasePo() {
    Arrays.stream(PurchaseRequestPO.class.getDeclaredFields())
        .forEach(field -> assertNotEquals(Boolean.class, field.getType()));
}
```

- [ ] **Step 2: 运行检查确认失败点**

Run: `rg -n "Boolean|boolean" xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po docs/sql`
Expected: 输出所有不符合约束的字段定义

- [ ] **Step 3: 补齐最小 SQL 与映射**

```sql
ALTER TABLE purchase_request
    ADD COLUMN draft_status INTEGER NULL,
    ADD COLUMN biz_status INTEGER NULL;
```

- [ ] **Step 4: 运行 purchase 模块测试**

Run: `mvn -pl xbb-erp-module-purchase test`
Expected: PASS

- [ ] **Step 5: 提交阶段性变更**

```bash
git add xbb-erp-module-purchase docs/sql docs/api
git commit -m "feat: add purchase draft persistence support"
```

### Task 6: 落地 admin-web 采购前端页面与路由

**Files:**
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/purchase/**`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/router.ts`
- Test: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/purchase/**/*.spec.ts`

**Interfaces:**
- Consumes: `customer` 现有页面模式、Task 2-5 产出的新接口协议
- Produces: 采购模块列表、新建编辑、详情、草稿、提交页面和路由

- [ ] **Step 1: 写失败的路由与页面挂载测试**

```ts
it('registers purchase routes', () => {
  expect(routes.some(route => route.path.includes('/purchase'))).toBe(true)
})
```

- [ ] **Step 2: 运行前端单测确认失败**

Run: `cd /Users/bomfish/xbb-ai-erp-v2-front && pnpm --filter admin-web test -- --runInBand router`
Expected: FAIL，提示 purchase 路由不存在

- [ ] **Step 3: 以 customer 页面为模板创建 purchase 页面、服务与类型**

```ts
export async function fetchPurchaseRequestList(payload: PurchaseRequestListDTO) {
  return request.post('/erp/v1/purchase-request/list', payload)
}
```

- [ ] **Step 4: 运行前端模块测试**

Run: `cd /Users/bomfish/xbb-ai-erp-v2-front && pnpm --filter admin-web test -- --runInBand purchase`
Expected: PASS

- [ ] **Step 5: 手工运行前端并验证黄金路径**

Run: `cd /Users/bomfish/xbb-ai-erp-v2-front && pnpm --filter admin-web dev`
Expected: 本地可访问采购列表、新建、草稿、提交、详情、删除主路径

- [ ] **Step 6: 提交阶段性变更**

```bash
git add /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src
git commit -m "feat: add purchase admin pages"
```

### Task 7: 整体验证并生成接口文档

**Files:**
- Modify: `docs/api/purchase-*.md`
- Modify: `docs/superpowers/plans/2026-08-04-module-purchase-customer-alignment.md`

**Interfaces:**
- Consumes: 全部实现结果
- Produces: 可验证的后端、前端、文档最终态

- [ ] **Step 1: 跑后端回归**

Run: `mvn -pl xbb-erp-module-purchase test`
Expected: PASS

- [ ] **Step 2: 跑前端回归**

Run: `cd /Users/bomfish/xbb-ai-erp-v2-front && pnpm --filter admin-web test -- --runInBand purchase router`
Expected: PASS

- [ ] **Step 3: 执行接口文档技能**

Run: `按项目要求执行 gen-api-md`
Expected: `purchase` 相关接口文档已更新

- [ ] **Step 4: 检查差异范围**

Run: `git status --short`
Expected: 仅包含 purchase 后端、前端、SQL、API 文档与计划文档相关变更

- [ ] **Step 5: 提交最终变更**

```bash
git add xbb-erp-module-purchase /Users/bomfish/xbb-ai-erp-v2-front docs/api docs/sql docs/superpowers/plans
git commit -m "feat: align purchase module with customer flow"
```
