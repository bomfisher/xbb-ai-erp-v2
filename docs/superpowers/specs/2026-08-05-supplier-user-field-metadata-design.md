# Supplier User Field Metadata Design

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Unify supplier form and list metadata so `ownerPurchaserId` uses the shared `USER(12)` field type and supplier classification/status fields use fixed option metadata.

**Architecture:** `module-org` provides a reusable member-single-select query for active, employed users. `module-supplier` owns supplier business enums and exposes them through `headList` and list metadata with fixed `itemList` values. The front end renders `USER(12)` with a generic member selector and renders the supplier enums with generic combo/select components.

**Tech Stack:** Spring Boot 3.3.2, JDK 21, MyBatis-Plus, Maven, Vue front end, shared field metadata protocol.

## Global Constraints

- 项目架构DDD领域驱动设计
- 对话永远在中文语境下，注释使用中文
- 所有接口的参数返回，都使用`ResultVO.success()`包装返回
- 所有接口的参数返回，都使用`ResultVO.success()`包装返回
- 所有接口的参数返回，都使用`ResultVO.success()`包装返回
- 非脚本接口，入参DTO都需要继承`BaseDTO`
- 系统内pojo尾缀规范：对接前端入参DTO、对接接口出参VO。其余中转参数的对象Pojo
- 直接对接数据库的对象实体需要添加`PO`后缀，并且对象内字段不允许使用布尔值对接，改用`Integer`
- 枚举类需要`Enum`结尾
- getter setter用Lombok管理
- `userId` 员工Id是字符串id
- 项目模块说明文档 `docs/base/项目业务module导航.md`

---

### Task 1: Define shared member selector contract

**Files:**
- Modify: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldTypeEnum.java`
- Modify: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldEntity.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/dto/MemberSelectListDTO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/vo/MemberSelectItemVO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/MemberSelectAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/MemberSelectAppService.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/MemberSelectAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/admin/MemberSelectAdminControllerStructureTest.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/application/service/MemberSelectAppServiceImplTest.java`

**Interfaces:**
- Consumes: `FieldTypeEnum.USER(12)` from the shared field protocol.
- Produces: a reusable org member selector endpoint that returns only active, employed employees with `id`, `name`, and `label`.

- [ ] **Step 1: Write the failing test**

```java
@Test
void memberSelectOnlyReturnsActiveEmployedEmployees() {
    MemberSelectListDTO dto = new MemberSelectListDTO();
    dto.setCorpid("corp-001");
    dto.setKeyword("张三");

    ResultVO<ListBaseVO<MemberSelectItemVO>> result = memberSelectAdminController.list(dto);

    assertEquals(0, result.getCode());
    assertTrue(result.getData().getList().stream().allMatch(item -> item.getEmploymentStatus().equals("ACTIVE") && item.getUserStatus().equals(1)));
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -pl xbb-erp-module-org -Dtest=MemberSelectAdminControllerStructureTest,MemberSelectAppServiceImplTest test`
Expected: FAIL because the selector endpoint and DTO/VO do not exist yet.

- [ ] **Step 3: Write minimal implementation**

```java
@GetMapping("/member-select/list")
public ResultVO<ListBaseVO<MemberSelectItemVO>> list(@RequestBody MemberSelectListDTO dto) {
    return ResultVO.success(memberSelectAppService.list(dto));
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -pl xbb-erp-module-org -Dtest=MemberSelectAdminControllerStructureTest,MemberSelectAppServiceImplTest test`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldTypeEnum.java \
        xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldEntity.java \
        xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/dto/MemberSelectListDTO.java \
        xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/vo/MemberSelectItemVO.java \
        xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/MemberSelectAdminController.java \
        xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/MemberSelectAppService.java \
        xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/MemberSelectAppServiceImpl.java \
        xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/admin/MemberSelectAdminControllerStructureTest.java \
        xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/application/service/MemberSelectAppServiceImplTest.java
```

### Task 2: Normalize supplier form metadata

**Files:**
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImpl.java`
- Modify: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImplTest.java`

**Interfaces:**
- Consumes: member selector contract for `ownerPurchaserId`.
- Produces: supplier save-item head metadata with `ownerPurchaserId=USER(12)` and fixed combo metadata for the four supplier business fields.

- [ ] **Step 1: Write the failing test**

```java
@Test
void addItemHeadListUsesUserFieldForOwnerPurchaserAndCombosForSupplierEnums() {
    SupplierSaveItemVO vo = supplierQueryAppService.addItem(new IdBaseDTO("corp-001"));

    FieldEntity ownerPurchaser = findHead(vo, "main.ownerPurchaserId");
    assertEquals(String.valueOf(FieldTypeEnum.USER.getType()), ownerPurchaser.getFieldType());

    FieldEntity supplierCategory = findHead(vo, "main.supplierCategory");
    assertEquals(String.valueOf(FieldTypeEnum.COMB.getType()), supplierCategory.getFieldType());
    assertFalse(supplierCategory.getItemList().isEmpty());
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierQueryAppServiceImplTest test`
Expected: FAIL because the current `headList` still marks all fields as `TEXT`.

- [ ] **Step 3: Write minimal implementation**

```java
private FieldEntity buildHead(String attr, String attrName, Integer required, FieldTypeEnum fieldType, List<FieldItem> itemList) {
    FieldEntity field = new FieldEntity();
    field.setAttr(attr);
    field.setAttrName(attrName);
    field.setFieldType(String.valueOf(fieldType.getType()));
    field.setRequired(required);
    field.setEditable(1);
    field.setItemList(itemList);
    return field;
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierQueryAppServiceImplTest test`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImpl.java \
        xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImplTest.java
```

### Task 3: Align supplier list metadata

**Files:**
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/provider/SupplierListMetaProvider.java`
- Modify: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/provider/SupplierListMetaProviderTest.java`

**Interfaces:**
- Consumes: fixed supplier enums and member selector field type.
- Produces: list filter and header metadata that matches the form metadata.

- [ ] **Step 1: Write the failing test**

```java
@Test
void listMetaIncludesMainBusinessCategoryAndMemberSelectorFieldType() {
    Map<String, ListFilterMetaPojo> metaMap = supplierListMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO());

    assertTrue(metaMap.containsKey("mainBusinessCategory"));
    assertEquals("ID", metaMap.get("ownerPurchaserId").getFieldType());
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierListMetaProviderTest test`
Expected: FAIL because `mainBusinessCategory` is missing from filter definitions.

- [ ] **Step 3: Write minimal implementation**

```java
new SupplierListFilterDefinition("mainBusinessCategory", "主营业务分类", "TEXT", "main_business_category", TEXT_SYMBOLS, List.of())
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierListMetaProviderTest test`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/provider/SupplierListMetaProvider.java \
        xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/provider/SupplierListMetaProviderTest.java
```

### Task 4: Wire supplier save-time snapshot refresh

**Files:**
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImplTest.java`

**Interfaces:**
- Consumes: org member selector service or repository lookup.
- Produces: persisted `ownerPurchaserNameSnapshot` that matches the selected employee.

- [ ] **Step 1: Write the failing test**

```java
@Test
void saveDraftRefreshesOwnerPurchaserNameSnapshotFromSelectedUser() {
    SupplierMainDTO dto = new SupplierMainDTO();
    dto.setOwnerPurchaserId("EMP-1001");

    supplierAdminAppService.saveDraft(dto);

    assertEquals("张三", persistedSupplier.getOwnerPurchaserNameSnapshot());
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierAdminAppServiceImplTest test`
Expected: FAIL because save currently does not refresh the snapshot.

- [ ] **Step 3: Write minimal implementation**

```java
supplier.setOwnerPurchaserNameSnapshot(memberSelectAppService.findNameById(dto.getOwnerPurchaserId()));
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierAdminAppServiceImplTest test`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImpl.java \
        xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImplTest.java
```

## Spec Review Checklist

- [x] No placeholder text remains
- [x] `USER(12)` is used only for the shared member selector contract
- [x] Supplier form, list filter, and header metadata are aligned on the same five fields
- [x] The scope is focused on supplier metadata and the shared member selector only
- [x] Each task has a test-first implementation cycle with concrete commands
