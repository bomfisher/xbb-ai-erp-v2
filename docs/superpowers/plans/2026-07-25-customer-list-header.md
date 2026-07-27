# Customer List Header Extraction Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将客户列表表头从客户 `list` 接口中抽离到公共列表 `header` 接口，且客户 `list` 不再返回 `headList`。

**Architecture:** 在 `xbb-erp-module-common` 的公共列表接口中新增 `/header` 元数据入口，继续复用 `ListCommonQueryDTO + businessCode` 分发到各业务 provider。客户模块复用现有字段工厂和字段装配逻辑提供表头元数据；客户 `list` 接口仅返回数据列表和分页信息。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、Lombok、JUnit 5

## Global Constraints

- 对话永远在中文语境下。
- 项目架构DDD领域驱动设计。
- 运行时：`JDK 21`
- 应用框架：`Spring Boot 3.3.2`
- 构建工具：`Maven`
- Web：`Spring Web`
- ORM：`MyBatis-Plus`
- 数据库：本地`MySQL 5.6` 其余环境`MySQL 8.0`
- 数据库迁移：`Flyway`
- 缓存：`Redis 7`
- 日志：`Logback`
- 测试：`JUnit 5 + Testcontainers`
- 枚举类需要 `Enum` 结尾。
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`，其余中转参数对象 `Pojo`。
- 所有接口入参都使用 DTO；非脚本接口 DTO 需要继承 `BaseDTO`。
- getter/setter 用 Lombok 管理。
- 本次只抽离客户列表表头，不顺带重构其它列表元数据能力。

---

### Task 1: 公共列表新增 header 元数据接口

**Files:**
- Modify: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`
- Modify: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListHeaderVO.java`

**Interfaces:**
- Consumes: `ListCommonQueryDTO`, `ListMetaRegistry#getRequiredProvider(String)`
- Produces: `ListCommonService#header(ListCommonQueryDTO): ListHeaderVO`, `ListCommonController#header(ListCommonQueryDTO): ListHeaderVO`, `ListMetaProvider#buildHeaderMeta(ListCommonQueryDTO): List<FieldEntity>`

- [ ] **Step 1: 写失败测试，先锁定公共服务和控制器需要支持 header**

```java
@Test
void should_dispatch_header_meta_to_provider_by_business_code() {
    ListMetaProvider provider = new StubListMetaProvider();
    ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
    ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

    ListHeaderVO headerVO = service.header(dto);

    assertEquals("main.customerCode", headerVO.getList().get(0).getAttr());
}
```

```java
@Test
void should_use_list_common_query_dto_for_all_four_endpoints() throws Exception {
    Class<?> dtoClass = Class.forName("xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO");

    Method filter = ListCommonController.class.getMethod("filter", dtoClass);
    Method header = ListCommonController.class.getMethod("header", dtoClass);
    Method topButton = ListCommonController.class.getMethod("topButton", dtoClass);
    Method bottomButton = ListCommonController.class.getMethod("bottomButton", dtoClass);

    assertNotNull(filter);
    assertNotNull(header);
    assertNotNull(topButton);
    assertNotNull(bottomButton);
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
Expected: FAIL，提示 `header` 方法或 `ListHeaderVO` 不存在。

- [ ] **Step 3: 写最小实现**

```java
@Data
public class ListHeaderVO {
    private List<FieldEntity> list;
}
```

```java
public interface ListMetaProvider {
    List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto);
}
```

```java
public interface ListCommonService {
    ListHeaderVO header(ListCommonQueryDTO dto);
}
```

```java
@Override
public ListHeaderVO header(ListCommonQueryDTO dto) {
    ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
    ListHeaderVO vo = new ListHeaderVO();
    vo.setList(provider.buildHeaderMeta(dto));
    return vo;
}
```

```java
@PostMapping("/header")
public ListHeaderVO header(@RequestBody ListCommonQueryDTO dto) {
    return listCommonService.header(dto);
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
Expected: PASS。

### Task 2: 客户 provider 输出 header 元数据

**Files:**
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`

**Interfaces:**
- Consumes: `CustomerFieldFactory#getFields(SceneTypeEnum.LIST)`, `CustomerFieldAssembler#buildHeadList(List<? extends SceneFieldMeta>)`
- Produces: `CustomerListMetaProvider#buildHeaderMeta(ListCommonQueryDTO): List<FieldEntity>`

- [ ] **Step 1: 写失败测试，锁定客户 provider 能产出 header**

```java
@Test
void should_build_customer_header_meta() {
    ListMetaProvider provider = new CustomerListMetaProvider();
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

    List<FieldEntity> headerList = provider.buildHeaderMeta(dto);

    assertFalse(headerList.isEmpty());
    assertEquals("main.customerCode", headerList.get(0).getAttr());
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest test`
Expected: FAIL，提示 `buildHeaderMeta` 不存在。

- [ ] **Step 3: 写最小实现**

```java
@Override
public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
    return CustomerFieldAssembler.buildHeadList(customerFieldFactory.getFields(SceneTypeEnum.LIST));
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest test`
Expected: PASS。

### Task 3: 客户 list 接口去掉 headList

**Files:**
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`

**Interfaces:**
- Consumes: `CustomerAdminAppServiceImpl#list(CustomerListDTO): ListBaseVO<CustomerListItemVO>`
- Produces: 客户 `list` 只返回 `list` 与 `pageHelper`，`headList == null`

- [ ] **Step 1: 写失败测试，锁定 list 不再返回 headList**

```java
@Test
void should_not_return_head_list_in_customer_list_response() {
    CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
        new FakeCustomerRepository(List.of()),
        new FakeCustomerContactRepository(List.of()),
        null,
        null,
        null,
        new DefaultCustomerFieldFactory(List.of())
    );
    CustomerListDTO dto = new CustomerListDTO();
    dto.setCorpid("corp-001");
    dto.setPageNum(1);
    dto.setPageSize(20);

    ListBaseVO<CustomerListItemVO> result = service.list(dto);

    assertEquals(null, result.getHeadList());
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListServiceTest test`
Expected: FAIL，当前 `headList` 仍被赋值。

- [ ] **Step 3: 写最小实现**

```java
@Override
public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
    // 保留原查询和组装逻辑
    ListBaseVO<CustomerListItemVO> vo = new ListBaseVO<>();
    vo.setList(list);
    vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), dto.getPageNum()));
    return vo;
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListServiceTest test`
Expected: PASS。

### Task 4: 收口验证

**Files:**
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`

**Interfaces:**
- Consumes: 前 3 个任务输出
- Produces: 公共 header 接口和客户 list 拆分行为验证闭环

- [ ] **Step 1: 跑公共模块定向测试**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
Expected: PASS。

- [ ] **Step 2: 跑客户模块定向测试**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest,CustomerListServiceTest test`
Expected: PASS。

- [ ] **Step 3: 跑关联模块编译**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -am -DskipTests compile`
Expected: PASS。

- [ ] **Step 4: 如接口出入参发生变化，执行 `gen-api-md` 技能更新接口文档**

Expected: 为新增 `/erp/v1/common/list/header` 与客户 `list` 出参变化补充文档。
