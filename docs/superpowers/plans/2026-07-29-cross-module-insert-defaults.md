# Cross Module Insert Defaults Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在不扩散到更新、删除和仓储统一兜底的前提下，只修复新增保存时基础字段未补齐的问题，并把实际代码修改收敛到 `purchase` 模块的命中保存入口。

**Architecture:** 先按现状确认 `supplier` 与 `product` 不落代码变更：`supplier` 当前只有 `Vendor` 主档保存，没有客户那种四个子档整单保存；`product` 已核实的 `Warehouse` 在应用服务层补齐基础字段，`ProductBrand`、`ProductCategory`、`ProductUnit`、`ProductSpu`、`ProductSku` 在仓储保存层已补齐新增基础字段。本次实现只修改 `purchase` 模块六个 `save(...)` 入口，在 `id == null` 的新增分支内补齐缺失基础字段，并通过轻量内存仓储测试锁定行为。

**Tech Stack:** JDK 21, Spring Boot 3.3.2, Maven, Spring Web, MyBatis-Plus, JUnit 5

## Global Constraints

- 对话永远在中文语境下，注释使用中文。
- 项目架构遵循 DDD 领域驱动设计。
- 运行时使用 `JDK 21`。
- 后端应用框架使用 `Spring Boot 3.3.2`。
- 后端构建工具使用 `Maven`。
- 所有主动捕获的报错、业务主动抛错都使用 `BizException`。
- 所有接口 DTO 作为参数，非脚本接口入参 DTO 都需要继承 `BaseDTO`。
- 如果接口业务代码没有需要返回的，用 `BaseVO` 返回。
- 所有接口参数返回都使用 `ResultVO.success()` 包装返回。
- `userId` 员工 Id 是字符串 id。
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`、其余中转参数使用 `Pojo`。
- 直接对接数据库的对象实体必须使用 `PO` 后缀，且对象内字段不允许使用布尔值对接，改用 `Integer`。
- 枚举类需要使用 `Enum` 结尾。
- 本次只检查并修复“新增保存时基础字段未补齐”，不扩散到 `update`、`remove`、数据库结构、接口入参出参或跨模块公共抽象。
- 只补缺，不覆盖调用方已显式传入的值。
- `supplier`、`product` 在本次实现中只作为已核实现状，不新增代码改动。
- 完成代码并通过验证后，如果接口出入参或 URL 发生变化，再执行 `gen-api-md` 技能；本次预计无接口协议变化。

---

### Task 1: 为采购主档保存补齐新增默认字段

**Files:**
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseMainSaveDefaultsServiceTest.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestRepository.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderRepository.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderAdminAppServiceImpl.java`

**Interfaces:**
- Consumes: `PurchaseRequestAdminAppServiceImpl#save(PurchaseRequestSaveDTO dto): Long`
- Consumes: `PurchaseOrderAdminAppServiceImpl#save(PurchaseOrderSaveDTO dto): Long`
- Produces: 新增分支在 `insert(...)` 前补齐：
  - `PurchaseRequest`：`bizStatus`、`version`、`deleted`、`addTime`、`updateTime`、`creatorId`、`modifyId`
  - `PurchaseOrder`：`bizStatus`、`version`、`deleted`、`addTime`、`updateTime`、`creatorId`、`modifyId`
- Produces: 仅当字段为空时补默认值；调用方已有值时保持原值

- [ ] **Step 1: 先写失败测试，锁定采购申请主档与采购订单主档的新增默认值**

```java
@Test
void should_apply_insert_defaults_for_purchase_request() {
    InMemoryPurchaseRequestRepository repository = new InMemoryPurchaseRequestRepository();
    PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(repository);

    PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    main.setPurchaseOrgId(10L);
    main.setRequestNo("PR-001");
    main.setApplicantId("emp-001");

    PurchaseRequestSaveDTO dto = new PurchaseRequestSaveDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setMain(main);

    Long savedId = service.save(dto);
    PurchaseRequest saved = repository.all().get(0);

    assertEquals(savedId, saved.getId());
    assertEquals("1", saved.getBizStatus());
    assertEquals(0, saved.getVersion());
    assertEquals(0, saved.getDeleted());
    assertEquals("user-001", saved.getCreatorId());
    assertEquals("user-001", saved.getModifyId());
    assertNotNull(saved.getAddTime());
    assertNotNull(saved.getUpdateTime());
}
```

```java
@Test
void should_apply_insert_defaults_for_purchase_order() {
    InMemoryPurchaseOrderRepository repository = new InMemoryPurchaseOrderRepository();
    PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(repository);

    PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
    main.setPurchaseOrgId(10L);
    main.setOrderNo("PO-001");
    main.setVendorId(20L);
    main.setVendorNameSnapshot("杭州供应商");

    PurchaseOrderSaveDTO dto = new PurchaseOrderSaveDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setMain(main);

    Long savedId = service.save(dto);
    PurchaseOrder saved = repository.all().get(0);

    assertEquals(savedId, saved.getId());
    assertEquals("1", saved.getBizStatus());
    assertEquals(0, saved.getVersion());
    assertEquals(0, saved.getDeleted());
    assertEquals("user-001", saved.getCreatorId());
    assertEquals("user-001", saved.getModifyId());
    assertNotNull(saved.getAddTime());
    assertNotNull(saved.getUpdateTime());
}
```

- [ ] **Step 2: 运行测试，确认当前主档新增分支确实缺默认值**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseMainSaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: FAIL，断言 `bizStatus`、`version`、`deleted` 或审计字段存在 `null`。

- [ ] **Step 3: 写最小内存仓储支撑测试，不引入数据库依赖**

```java
class InMemoryPurchaseRequestRepository implements PurchaseRequestRepository {
    private final AtomicLong sequence = new AtomicLong(1L);
    private final List<PurchaseRequest> store = new ArrayList<>();

    @Override
    public void insert(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getId() == null) {
            purchaseRequest.setId(sequence.getAndIncrement());
        }
        store.add(copy(purchaseRequest));
    }

    @Override
    public void update(PurchaseRequest purchaseRequest) {
        removeById(purchaseRequest.getCorpid(), purchaseRequest.getId());
        store.add(copy(purchaseRequest));
    }

    List<PurchaseRequest> all() {
        return store.stream().map(this::copy).toList();
    }

    // 其余接口返回最小实现：findById / findByCondition / count / removeBatchByIds
}
```

```java
class InMemoryPurchaseOrderRepository implements PurchaseOrderRepository {
    private final AtomicLong sequence = new AtomicLong(1L);
    private final List<PurchaseOrder> store = new ArrayList<>();

    @Override
    public void insert(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getId() == null) {
            purchaseOrder.setId(sequence.getAndIncrement());
        }
        store.add(copy(purchaseOrder));
    }

    List<PurchaseOrder> all() {
        return store.stream().map(this::copy).toList();
    }

    // 其余接口同样提供最小实现
}
```

- [ ] **Step 4: 在主档保存服务的新增分支补齐缺失字段**

```java
@Override
public Long save(PurchaseRequestSaveDTO dto) {
    PurchaseRequest purchaseRequest = PurchaseRequestAdminAssembler.toPurchaseRequest(dto);
    if (purchaseRequest.getId() == null) {
        applyInsertDefaults(purchaseRequest, dto.getUserId());
        purchaseRequestRepository.insert(purchaseRequest);
    } else {
        purchaseRequestRepository.update(purchaseRequest);
    }
    return purchaseRequest.getId();
}

private void applyInsertDefaults(PurchaseRequest purchaseRequest, String userId) {
    long now = System.currentTimeMillis();
    if (purchaseRequest.getBizStatus() == null || purchaseRequest.getBizStatus().isBlank()) {
        purchaseRequest.setBizStatus("1");
    }
    if (purchaseRequest.getVersion() == null) {
        purchaseRequest.setVersion(0);
    }
    if (purchaseRequest.getDeleted() == null) {
        purchaseRequest.setDeleted(0);
    }
    if (purchaseRequest.getAddTime() == null) {
        purchaseRequest.setAddTime(now);
    }
    if (purchaseRequest.getUpdateTime() == null) {
        purchaseRequest.setUpdateTime(now);
    }
    if (purchaseRequest.getCreatorId() == null || purchaseRequest.getCreatorId().isBlank()) {
        purchaseRequest.setCreatorId(userId);
    }
    if (purchaseRequest.getModifyId() == null || purchaseRequest.getModifyId().isBlank()) {
        purchaseRequest.setModifyId(userId);
    }
}
```

```java
@Override
public Long save(PurchaseOrderSaveDTO dto) {
    PurchaseOrder purchaseOrder = PurchaseOrderAdminAssembler.toPurchaseOrder(dto);
    if (purchaseOrder.getId() == null) {
        applyInsertDefaults(purchaseOrder, dto.getUserId());
        purchaseOrderRepository.insert(purchaseOrder);
    } else {
        purchaseOrderRepository.update(purchaseOrder);
    }
    return purchaseOrder.getId();
}
```

- [ ] **Step 5: 再跑主档测试，确认红绿闭环**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseMainSaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: PASS，两个主档新增保存都能补齐默认值且不影响更新分支。

- [ ] **Step 6: 提交主档这一小步**

```bash
git add \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseMainSaveDefaultsServiceTest.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderRepository.java

git commit -m "fix: complete insert defaults for purchase main saves"
```

### Task 2: 为采购行项目与辅助关系保存补齐新增默认字段

**Files:**
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseSubEntitySaveDefaultsServiceTest.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestItemRepository.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderItemRepository.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchasePendingTaskRepository.java`
- Create: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseSourceRelationRepository.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestItemAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderItemAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchasePendingTaskAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseSourceRelationAdminAppServiceImpl.java`

**Interfaces:**
- Consumes: 四个 `save(...)` 入口：
  - `PurchaseRequestItemAdminAppServiceImpl#save(PurchaseRequestItemSaveDTO dto): Long`
  - `PurchaseOrderItemAdminAppServiceImpl#save(PurchaseOrderItemSaveDTO dto): Long`
  - `PurchasePendingTaskAdminAppServiceImpl#save(PurchasePendingTaskSaveDTO dto): Long`
  - `PurchaseSourceRelationAdminAppServiceImpl#save(PurchaseSourceRelationSaveDTO dto): Long`
- Produces: 新增分支在 `insert(...)` 前补齐 `version`、`deleted`、`addTime`、`updateTime`、`creatorId`、`modifyId`
- Produces: `PurchasePendingTask#taskStatus`、`PurchaseSourceRelation#relationStatus`、各数量与快照字段保持调用方原值，不擅自重写

- [ ] **Step 1: 先写失败测试，分别锁定四个非主档新增分支的默认值**

```java
@Test
void should_apply_insert_defaults_for_purchase_request_item() {
    InMemoryPurchaseRequestItemRepository repository = new InMemoryPurchaseRequestItemRepository();
    PurchaseRequestItemAdminAppServiceImpl service = new PurchaseRequestItemAdminAppServiceImpl(repository);

    PurchaseRequestItemMainDTO main = new PurchaseRequestItemMainDTO();
    main.setRequestId(100L);
    main.setLineNo(1);
    main.setSkuId(1000L);
    main.setRequestQty(BigDecimal.ONE);

    PurchaseRequestItemSaveDTO dto = new PurchaseRequestItemSaveDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setMain(main);

    service.save(dto);
    PurchaseRequestItem saved = repository.all().get(0);

    assertEquals(0, saved.getVersion());
    assertEquals(0, saved.getDeleted());
    assertEquals("user-001", saved.getCreatorId());
    assertEquals("user-001", saved.getModifyId());
    assertNotNull(saved.getAddTime());
    assertNotNull(saved.getUpdateTime());
}
```

```java
@Test
void should_apply_insert_defaults_for_purchase_order_item() { /* 同样断言 version/deleted/audit 字段 */ }

@Test
void should_apply_insert_defaults_for_purchase_pending_task() { /* 同样断言 version/deleted/audit 字段，保留 taskStatus 原值 */ }

@Test
void should_apply_insert_defaults_for_purchase_source_relation() { /* 同样断言 version/deleted/audit 字段，保留 relationStatus 原值 */ }
```

- [ ] **Step 2: 运行测试，确认当前四个入口都缺少新增默认值**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseSubEntitySaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: FAIL，至少一个断言显示 `version`、`deleted`、`creatorId`、`modifyId` 或时间字段为空。

- [ ] **Step 3: 补齐四个内存仓储支撑，模式与主档测试保持一致**

```java
class InMemoryPurchaseRequestItemRepository implements PurchaseRequestItemRepository {
    private final AtomicLong sequence = new AtomicLong(1L);
    private final List<PurchaseRequestItem> store = new ArrayList<>();

    @Override
    public void insert(PurchaseRequestItem purchaseRequestItem) {
        if (purchaseRequestItem.getId() == null) {
            purchaseRequestItem.setId(sequence.getAndIncrement());
        }
        store.add(copy(purchaseRequestItem));
    }

    List<PurchaseRequestItem> all() {
        return store.stream().map(this::copy).toList();
    }
}
```

```java
class InMemoryPurchasePendingTaskRepository implements PurchasePendingTaskRepository {
    private final AtomicLong sequence = new AtomicLong(1L);
    private final List<PurchasePendingTask> store = new ArrayList<>();

    @Override
    public void insert(PurchasePendingTask purchasePendingTask) {
        if (purchasePendingTask.getId() == null) {
            purchasePendingTask.setId(sequence.getAndIncrement());
        }
        store.add(copy(purchasePendingTask));
    }

    List<PurchasePendingTask> all() {
        return store.stream().map(this::copy).toList();
    }
}
```

- [ ] **Step 4: 在四个新增分支补齐最小默认值，不扩散到更新逻辑**

```java
@Override
public Long save(PurchaseRequestItemSaveDTO dto) {
    PurchaseRequestItem purchaseRequestItem = PurchaseRequestItemAdminAssembler.toPurchaseRequestItem(dto);
    if (purchaseRequestItem.getId() == null) {
        applyInsertDefaults(purchaseRequestItem, dto.getUserId());
        purchaseRequestItemRepository.insert(purchaseRequestItem);
    } else {
        purchaseRequestItemRepository.update(purchaseRequestItem);
    }
    return purchaseRequestItem.getId();
}

private void applyInsertDefaults(PurchaseRequestItem purchaseRequestItem, String userId) {
    long now = System.currentTimeMillis();
    if (purchaseRequestItem.getVersion() == null) {
        purchaseRequestItem.setVersion(0);
    }
    if (purchaseRequestItem.getDeleted() == null) {
        purchaseRequestItem.setDeleted(0);
    }
    if (purchaseRequestItem.getAddTime() == null) {
        purchaseRequestItem.setAddTime(now);
    }
    if (purchaseRequestItem.getUpdateTime() == null) {
        purchaseRequestItem.setUpdateTime(now);
    }
    if (purchaseRequestItem.getCreatorId() == null || purchaseRequestItem.getCreatorId().isBlank()) {
        purchaseRequestItem.setCreatorId(userId);
    }
    if (purchaseRequestItem.getModifyId() == null || purchaseRequestItem.getModifyId().isBlank()) {
        purchaseRequestItem.setModifyId(userId);
    }
}
```

```java
private void applyInsertDefaults(PurchasePendingTask purchasePendingTask, String userId) {
    long now = System.currentTimeMillis();
    if (purchasePendingTask.getVersion() == null) {
        purchasePendingTask.setVersion(0);
    }
    if (purchasePendingTask.getDeleted() == null) {
        purchasePendingTask.setDeleted(0);
    }
    if (purchasePendingTask.getAddTime() == null) {
        purchasePendingTask.setAddTime(now);
    }
    if (purchasePendingTask.getUpdateTime() == null) {
        purchasePendingTask.setUpdateTime(now);
    }
    if (purchasePendingTask.getCreatorId() == null || purchasePendingTask.getCreatorId().isBlank()) {
        purchasePendingTask.setCreatorId(userId);
    }
    if (purchasePendingTask.getModifyId() == null || purchasePendingTask.getModifyId().isBlank()) {
        purchasePendingTask.setModifyId(userId);
    }
}
```

```java
private void applyInsertDefaults(PurchaseSourceRelation purchaseSourceRelation, String userId) {
    long now = System.currentTimeMillis();
    if (purchaseSourceRelation.getVersion() == null) {
        purchaseSourceRelation.setVersion(0);
    }
    if (purchaseSourceRelation.getDeleted() == null) {
        purchaseSourceRelation.setDeleted(0);
    }
    if (purchaseSourceRelation.getAddTime() == null) {
        purchaseSourceRelation.setAddTime(now);
    }
    if (purchaseSourceRelation.getUpdateTime() == null) {
        purchaseSourceRelation.setUpdateTime(now);
    }
    if (purchaseSourceRelation.getCreatorId() == null || purchaseSourceRelation.getCreatorId().isBlank()) {
        purchaseSourceRelation.setCreatorId(userId);
    }
    if (purchaseSourceRelation.getModifyId() == null || purchaseSourceRelation.getModifyId().isBlank()) {
        purchaseSourceRelation.setModifyId(userId);
    }
}
```

- [ ] **Step 5: 再跑非主档测试，确认四个新增分支通过**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseSubEntitySaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: PASS，四个保存入口只在新增分支补齐基础字段。

- [ ] **Step 6: 提交行项目与辅助关系这一小步**

```bash
git add \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestItemAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderItemAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchasePendingTaskAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseSourceRelationAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseSubEntitySaveDefaultsServiceTest.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestItemRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderItemRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchasePendingTaskRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseSourceRelationRepository.java

git commit -m "fix: complete insert defaults for purchase sub-entity saves"
```

### Task 3: 做最终验证并保持跨模块范围收敛

**Files:**
- Modify: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseMainSaveDefaultsServiceTest.java`
- Modify: `xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseSubEntitySaveDefaultsServiceTest.java`
- No change expected: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/VendorAdminAppServiceImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/impl/WarehouseAdminAppServiceImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductBrandRepositoryImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductCategoryRepositoryImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductUnitRepositoryImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSpuRepositoryImpl.java`
- No change expected: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSkuRepositoryImpl.java`

**Interfaces:**
- Consumes: `PurchaseMainSaveDefaultsServiceTest`
- Consumes: `PurchaseSubEntitySaveDefaultsServiceTest`
- Produces: 最终验证结论——代码改动只落在 `purchase`，`supplier` 与 `product` 维持无修改

- [ ] **Step 1: 给测试补上“显式传值不覆盖”的断言，锁定只补缺行为**

```java
@Test
void should_keep_explicit_insert_fields_when_purchase_request_values_are_provided() {
    InMemoryPurchaseRequestRepository repository = new InMemoryPurchaseRequestRepository();
    PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(repository);

    PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
    main.setPurchaseOrgId(10L);
    main.setRequestNo("PR-002");
    main.setBizStatus("MANUAL");
    main.setVersion(8);
    main.setDeleted(0);
    main.setAddTime(100L);
    main.setUpdateTime(200L);
    main.setCreatorId("creator-x");
    main.setModifyId("modifier-y");

    PurchaseRequestSaveDTO dto = new PurchaseRequestSaveDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setMain(main);

    service.save(dto);
    PurchaseRequest saved = repository.all().get(0);

    assertEquals("MANUAL", saved.getBizStatus());
    assertEquals(8, saved.getVersion());
    assertEquals(100L, saved.getAddTime());
    assertEquals(200L, saved.getUpdateTime());
    assertEquals("creator-x", saved.getCreatorId());
    assertEquals("modifier-y", saved.getModifyId());
}
```

- [ ] **Step 2: 运行两组测试，确认最终行为与范围同时成立**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseMainSaveDefaultsServiceTest,PurchaseSubEntitySaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: PASS，新增分支补齐默认值、显式传值不被覆盖、更新分支未被触碰。

- [ ] **Step 3: 用 git 差异确认没有误改 supplier 与 product**

Run: `git diff --stat -- xbb-erp-module-supplier xbb-erp-module-product xbb-erp-module-purchase`
Expected: 只有 `xbb-erp-module-purchase` 下的服务与测试文件发生变更，`supplier`、`product` 无差异。

- [ ] **Step 4: 进行最终整体验证**

Run: `mvn -pl xbb-erp-module-purchase -am -Dtest=PurchaseMainSaveDefaultsServiceTest,PurchaseSubEntitySaveDefaultsServiceTest -Dsurefire.failIfNoSpecifiedTests=false test`
Expected: PASS，且控制台没有新增失败用例。

- [ ] **Step 5: 提交最终收口**

```bash
git add \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestItemAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseOrderItemAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchasePendingTaskAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseSourceRelationAdminAppServiceImpl.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseMainSaveDefaultsServiceTest.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/PurchaseSubEntitySaveDefaultsServiceTest.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseRequestItemRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseOrderItemRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchasePendingTaskRepository.java \
  xbb-erp-module-purchase/src/test/java/xbb/ai/erp/module/purchase/application/service/support/InMemoryPurchaseSourceRelationRepository.java

git commit -m "fix: complete insert defaults for purchase save flows"
```
