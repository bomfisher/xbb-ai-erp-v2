# Customer List Dynamic Filter Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将客户列表查询从平铺筛选字段协议切换为 `keyword + conditions` 新协议，并同时落地前后端可复用的动态筛选公共底座。

**Architecture:** 后端以 `xbb-erp-module-common` 提供统一筛选条件模型、字段元数据契约和条件构建器，客户模块只维护自己的字段白名单并在 `CustomerMapper.xml` 中引用公共动态 SQL 片段。前端以 `apps/admin-web/src/components/list/ListFilterPanel.vue` 为公共筛选组件入口，统一输出 `keyword` 与 `conditions`，客户列表页和模板页都改为消费这套新协议。

**Tech Stack:** JDK 21, Spring Boot 3.3.2, Maven, MyBatis XML, JUnit 5, pnpm@9.12.0, Vue 3, TypeScript 5, Vite 5, Vitest

## Global Constraints

- 对话永远在中文语境下，注释使用中文。
- 项目架构遵循 DDD 领域驱动设计。
- 运行时使用 `JDK 21`。
- 后端应用框架使用 `Spring Boot 3.3.2`。
- 后端构建工具使用 `Maven`。
- 后端测试使用 `JUnit 5 + Testcontainers`。
- 前端工程组织使用 `pnpm monorepo`，包管理器固定为 `pnpm@9.12.0`。
- 前端框架使用 `Vue 3`，路由使用 `vue-router 4`，构建工具使用 `Vite 5`，语言使用 `TypeScript 5`，测试使用 `Vitest`。
- 所有接口参数返回都使用 `ResultVO.success()` 包装返回。
- 所有接口 DTO 作为参数，非脚本接口入参 DTO 都需要继承 `BaseDTO`。
- `userId` 员工 Id 是字符串 id。
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`、其余中转参数使用 `Pojo`。
- 直接对接数据库的对象实体必须使用 `PO` 后缀，且对象内字段不允许使用布尔值对接，改用 `Integer`。
- 本次客户列表接口直接切新协议，不兼容旧平铺筛选字段。
- 前端公共筛选组件落在 `apps/admin-web` 内，不提升到 workspace 共享包。
- 完成代码并通过验证后，执行 `gen-api-md` 技能同步接口文档。

---

### Task 1: 扩展筛选元数据契约

**Files:**
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListFilterCondition.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/FilterField.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Modify: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

**Interfaces:**
- Consumes: `ListMetaProvider#buildFilterMeta(ListCommonQueryDTO dto): List<FilterField>`
- Produces: `FilterField#getFieldType(): String`
- Produces: `FilterField#getSupportedSymbols(): List<String>`
- Produces: `FilterField#getItemList(): List<FieldItem>`
- Produces: `ListFilterCondition` with `attr: String`, `fieldType: String`, `symbol: String`, `value: List<String>`

- [ ] **Step 1: 先写失败测试，锁定新的筛选元数据契约**

```java
@Test
void should_expose_supported_symbols_and_item_list_for_customer_filters() {
    ListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

    List<FilterField> filters = provider.buildFilterMeta(dto);
    FilterField bizStatus = filters.stream()
        .filter(item -> "bizStatus".equals(item.getAttr()))
        .findFirst()
        .orElseThrow();
    FilterField createTime = filters.stream()
        .filter(item -> "createTime".equals(item.getAttr()))
        .findFirst()
        .orElseThrow();

    assertEquals("ENUM", bizStatus.getFieldType());
    assertEquals(List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"), bizStatus.getSupportedSymbols());
    assertFalse(bizStatus.getItemList().isEmpty());
    assertEquals("DATE", createTime.getFieldType());
}
```

```java
@Test
void should_return_filter_field_with_string_field_type_and_supported_symbols() {
    ListMetaProvider provider = new StubListMetaProvider();
    ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
    ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setBusinessCode("CUSTOMER");

    ListFilterVO filterVO = service.filter(dto);

    assertEquals("TEXT", filterVO.getList().get(0).getFieldType());
    assertEquals(List.of("EQ", "CONTAINS"), filterVO.getList().get(0).getSupportedSymbols());
    assertEquals("客户编码", filterVO.getList().get(0).getItemList().get(0).getText());
}
```

- [ ] **Step 2: 运行测试，确认当前契约还不满足需求**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonServiceTest,CustomerListMetaProviderTest test`
Expected: FAIL，提示 `FilterField` 缺少 `supportedSymbols` / `itemList` 或 `fieldType` 类型不匹配，且 `CustomerListMetaProvider` 未返回 `createTime`。

- [ ] **Step 3: 只写最小实现，把筛选元数据契约补齐**

```java
@Data
public class FilterField {
    private String attr;
    private String attrName;
    private String fieldType;
    private List<String> supportedSymbols;
    private List<FieldItem> itemList;
}
```

```java
@Data
public class ListFilterCondition {
    private String attr;
    private String fieldType;
    private String symbol;
    private List<String> value;
}
```

```java
@Override
public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
    return List.of(
        buildFilterField("customerCode", "客户编码", "TEXT", List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"), List.of()),
        buildFilterField("customerName", "客户名称", "TEXT", List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"), List.of()),
        buildFilterField("customerCategory", "客户分类", "ENUM", List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"), List.of()),
        buildFilterField("regionCode", "所属区域", "ENUM", List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"), List.of()),
        buildFilterField("ownerSalesId", "归属销售", "ID", List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"), List.of()),
        buildFilterField("bizStatus", "业务状态", "ENUM", List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"), buildBizStatusItems()),
        buildFilterField("createTime", "创建时间", "DATE", List.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"), List.of())
    );
}
```

- [ ] **Step 4: 再跑测试，确认元数据契约闭环**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonServiceTest,CustomerListMetaProviderTest test`
Expected: PASS，`FilterField` 返回字符串 `fieldType`、运算符列表、选项列表，并且 `createTime` 已出现在客户列表筛选元数据中。

- [ ] **Step 5: 提交这一小步**

```bash
git add \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/FilterField.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListFilterCondition.java \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java

git commit -m "feat: extend list filter metadata contract"
```

### Task 2: 切换后端客户列表查询到 `keyword + conditions`

**Files:**
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterFieldTypeEnum.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterSymbolEnum.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterMetaPojo.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterConditionBuilder.java`
- Create: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/filter/ListFilterConditionBuilderTest.java`
- Create: `xbb-erp-module-common/src/main/resources/mapper/common/CommonListFilterMapper.xml`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerListDTO.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerMapper.xml`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerRepository.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/ConditionMapHelperTest.java`

**Interfaces:**
- Consumes: `CustomerAdminAppServiceImpl#list(CustomerListDTO dto): ListBaseVO<CustomerListItemVO>`
- Consumes: `CustomerRepository#findByCondition(Map<String, Object> conditionMap): List<Customer>`
- Produces: `ListFilterConditionBuilder#build(List<ListFilterCondition> rawConditions, Map<String, ListFilterMetaPojo> metaMap): List<ListFilterCondition>`
- Produces: `CustomerListDTO#getConditions(): List<ListFilterCondition>`
- Produces: `conditionMap.conditions` passed into `CustomerMapper.xml`

- [ ] **Step 1: 先写失败测试，锁定新 DTO、构建器和查询行为**

```java
@Test
void should_map_customer_list_conditions_to_safe_columns() {
    ListFilterConditionBuilder builder = new ListFilterConditionBuilder();
    ListFilterCondition raw = new ListFilterCondition();
    raw.setAttr("customerName");
    raw.setFieldType("TEXT");
    raw.setSymbol("CONTAINS");
    raw.setValue(List.of("杭州"));

    Map<String, ListFilterMetaPojo> metaMap = Map.of(
        "customerName",
        new ListFilterMetaPojo("customerName", "customer_name", "TEXT", Set.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"))
    );

    List<ListFilterCondition> conditions = builder.build(List.of(raw), metaMap);

    assertEquals("customer_name", conditions.get(0).getAttr());
}
```

```java
@Test
void should_apply_keyword_and_dynamic_conditions_with_paged_result() {
    Customer first = buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100");
    Customer second = buildCustomer(2L, "corp-001", "CUST-002", "杭州客户二", "330100");
    Customer third = buildCustomer(3L, "corp-001", "CUST-003", "宁波客户", "330200");

    CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
        new FakeCustomerRepository(List.of(first, second, third)),
        new FakeCustomerContactRepository(List.of()),
        null,
        null,
        null,
        new DefaultCustomerFieldFactory(List.of())
    );

    ListFilterCondition condition = new ListFilterCondition();
    condition.setAttr("regionCode");
    condition.setFieldType("ENUM");
    condition.setSymbol("EQ");
    condition.setValue(List.of("330100"));

    CustomerListDTO dto = new CustomerListDTO();
    dto.setCorpid("corp-001");
    dto.setKeyword("杭州");
    dto.setConditions(List.of(condition));
    dto.setPageNum(1);
    dto.setPageSize(1);

    ListBaseVO<CustomerListItemVO> result = service.list(dto);

    assertEquals(1, result.getList().size());
    assertEquals("CUST-001", result.getList().get(0).getCustomerCode());
    assertEquals(2, result.getPageHelper().getCount());
}
```

- [ ] **Step 2: 运行测试，确认旧查询路径不支持新协议**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListFilterConditionBuilderTest,CustomerListServiceTest,ConditionMapHelperTest test`
Expected: FAIL，提示 `CustomerListDTO` 缺少 `conditions`，`ListFilterConditionBuilder` 不存在，或 `FakeCustomerRepository` 仍依赖旧的平铺字段。

- [ ] **Step 3: 写最小实现，让服务层和 Mapper 都走 `conditions`**

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListDTO extends ListBaseDTO {
    private String keyword;
    private List<ListFilterCondition> conditions;
}
```

```java
public List<ListFilterCondition> build(List<ListFilterCondition> rawConditions, Map<String, ListFilterMetaPojo> metaMap) {
    if (rawConditions == null || rawConditions.isEmpty()) {
        return List.of();
    }
    return rawConditions.stream().map(condition -> {
        ListFilterMetaPojo meta = metaMap.get(condition.getAttr());
        if (meta == null) {
            throw new BizException("FILTER_ATTR_NOT_SUPPORTED");
        }
        if (!meta.getFieldType().equals(condition.getFieldType())) {
            throw new BizException("FILTER_FIELD_TYPE_MISMATCH");
        }
        if (!meta.getSupportedSymbols().contains(condition.getSymbol())) {
            throw new BizException("FILTER_SYMBOL_NOT_SUPPORTED");
        }
        validateValue(condition);
        ListFilterCondition safe = new ListFilterCondition();
        safe.setAttr(meta.getColumn());
        safe.setFieldType(condition.getFieldType());
        safe.setSymbol(condition.getSymbol());
        safe.setValue(condition.getValue());
        return safe;
    }).toList();
}
```

```java
private Map<String, Object> buildListConditionMap(CustomerListDTO dto) {
    Map<String, Object> conditionMap = new HashMap<>();
    conditionMap.put("corpid", dto.getCorpid());
    conditionMap.put("keyword", dto.getKeyword());
    conditionMap.put("conditions", listFilterConditionBuilder.build(dto.getConditions(), customerListFilterMetaMap()));
    return conditionMap;
}
```

```xml
<sql id="BaseCondition">
    corpid = #{conditionMap.corpid}
    and del = 0
    <if test="conditionMap.keyword != null and conditionMap.keyword != ''">
        and (
            customer_code like concat('%', #{conditionMap.keyword}, '%')
            or customer_name like concat('%', #{conditionMap.keyword}, '%')
        )
    </if>
</sql>

<select id="findByCondition" resultType="xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO">
    select <include refid="BaseColumns"/>
    from customer
    where <include refid="BaseCondition"/>
    <if test="conditionMap.conditions != null and conditionMap.conditions.size() > 0">
        <foreach collection="conditionMap.conditions" item="c">
            <include refid="CommonListFilterMapper.dynamicCondition"/>
        </foreach>
    </if>
    <include refid="QueryTail"/>
</select>
```

- [ ] **Step 4: 再跑测试，确认后端已经完成协议切换**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListFilterConditionBuilderTest,CustomerListServiceTest,ConditionMapHelperTest test`
Expected: PASS，`CustomerListDTO` 只保留 `keyword + conditions`，服务层会把业务字段映射为安全列名，分页结果仍然正确。

- [ ] **Step 5: 提交这一小步**

```bash
git add \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterFieldTypeEnum.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterSymbolEnum.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterMetaPojo.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/filter/ListFilterConditionBuilder.java \
  xbb-erp-module-common/src/main/resources/mapper/common/CommonListFilterMapper.xml \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/filter/ListFilterConditionBuilderTest.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerListDTO.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java \
  xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerMapper.xml \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerRepository.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/ConditionMapHelperTest.java

git commit -m "feat: switch customer list to dynamic filter protocol"
```

### Task 3: 抽取管理端公共动态筛选组件

**Files:**
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterConditionRow.vue`
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterPanel.spec.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterPanel.vue`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts`

**Interfaces:**
- Consumes: `type FilterField = { attr: string; attrName: string; fieldType: string; supportedSymbols: string[]; itemList: FieldOptionItem[] }`
- Produces: `type ListFilterCondition = { attr: string; fieldType: string; symbol: string; value: string[] }`
- Produces: `ListFilterPanel` props `filters: FilterField[]`, `keyword: string`, `conditions: ListFilterCondition[]`
- Produces: `ListFilterPanel` emits `update:keyword`, `update:conditions`, `submit`, `reset`

- [ ] **Step 1: 先写失败测试，锁定新组件的输入输出契约**

```ts
it('emits keyword and conditions using the new dynamic protocol', async () => {
  const wrapper = mount(ListFilterPanel, {
    props: {
      filters: [
        {
          attr: 'customerName',
          attrName: '客户名称',
          fieldType: 'TEXT',
          supportedSymbols: ['EQ', 'CONTAINS'],
          itemList: [],
        },
      ],
      keyword: '',
      conditions: [],
    },
  })

  await wrapper.get('[data-testid="filter-keyword"]').setValue('杭州')
  await wrapper.get('[data-testid="add-condition"]').trigger('click')
  await wrapper.get('[data-testid="condition-attr-0"]').setValue('customerName')
  await wrapper.get('[data-testid="condition-symbol-0"]').setValue('CONTAINS')
  await wrapper.get('[data-testid="condition-value-0-0"]').setValue('客户')

  expect(wrapper.emitted('update:keyword')?.at(-1)?.[0]).toBe('杭州')
  expect(wrapper.emitted('update:conditions')?.at(-1)?.[0]).toEqual([
    { attr: 'customerName', fieldType: 'TEXT', symbol: 'CONTAINS', value: ['客户'] },
  ])
})
```

```ts
it('hides the value input for IS_EMPTY symbol', async () => {
  const wrapper = mount(ListFilterPanel, {
    props: {
      filters: [
        {
          attr: 'bizStatus',
          attrName: '业务状态',
          fieldType: 'ENUM',
          supportedSymbols: ['EQ', 'IS_EMPTY'],
          itemList: [{ label: '启用', value: 'ENABLED' }],
        },
      ],
      keyword: '',
      conditions: [{ attr: 'bizStatus', fieldType: 'ENUM', symbol: 'IS_EMPTY', value: [] }],
    },
  })

  expect(wrapper.find('[data-testid="condition-value-0-0"]').exists()).toBe(false)
})
```

- [ ] **Step 2: 运行测试，确认旧组件还是 `queryForm` 时代的实现**

Run: `pnpm --filter @xbb-erp/admin-web test -- src/components/list/ListFilterPanel.spec.ts`
Expected: FAIL，提示 `ListFilterPanel` 没有 `keyword` / `conditions` props，且不存在条件行、运算符联动和多值输入。

- [ ] **Step 3: 写最小实现，让公共组件能真正输出 `keyword + conditions`**

```ts
export type FilterField = {
  attr: string
  attrName: string
  fieldType: string
  supportedSymbols: string[]
  itemList: FieldOptionItem[]
}

export type ListFilterCondition = {
  attr: string
  fieldType: string
  symbol: string
  value: string[]
}
```

```vue
<script setup lang="ts">
import ListFilterConditionRow from './ListFilterConditionRow.vue'
import type { FilterField, ListFilterCondition } from './types'

const props = defineProps<{
  filters: FilterField[]
  keyword: string
  conditions: ListFilterCondition[]
}>()

const emit = defineEmits<{
  'update:keyword': [value: string]
  'update:conditions': [value: ListFilterCondition[]]
  submit: []
  reset: []
}>()

function addCondition() {
  const first = props.filters[0]
  if (!first) return
  emit('update:conditions', [
    ...props.conditions,
    { attr: first.attr, fieldType: first.fieldType, symbol: first.supportedSymbols[0], value: [''] },
  ])
}
</script>
```

```vue
<script setup lang="ts">
import type { FilterField, ListFilterCondition } from './types'

const props = defineProps<{
  index: number
  filters: FilterField[]
  condition: ListFilterCondition
}>()

const emit = defineEmits<{
  update: [index: number, value: ListFilterCondition]
  remove: [index: number]
}>()
</script>
```

- [ ] **Step 4: 再跑测试，确认组件协议稳定**

Run: `pnpm --filter @xbb-erp/admin-web test -- src/components/list/ListFilterPanel.spec.ts`
Expected: PASS，组件可以输出 `keyword` 与 `conditions`，并且 `IS_EMPTY`、`BETWEEN`、`IN` 等场景能正确控制值输入区。

- [ ] **Step 5: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterPanel.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterConditionRow.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListFilterPanel.spec.ts

git commit -m "feat: add reusable dynamic list filter panel"
```

### Task 4: 接入客户列表页和模板页的新筛选协议

**Files:**
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/template/list/ListPageTemplate.vue`

**Interfaces:**
- Consumes: `ListFilterPanel` props `filters`, `keyword`, `conditions`
- Consumes: `/erp/v1/customer/list` payload `{ corpid, userId, pageNum, pageSize, keyword, conditions }`
- Produces: `CustomerListPage` no longer keeps `queryForm: Record<string, string>`
- Produces: template page example that future lists can copy with `keyword + conditions`

- [ ] **Step 1: 先写失败测试，锁定客户列表页的新请求体和页面状态**

```ts
it('posts only keyword and conditions to customer list', async () => {
  fetchMock.mockImplementation(async (input: RequestInfo | URL, init?: RequestInit) => {
    const url = String(input)
    if (url.endsWith('/erp/v1/common/list/filter')) {
      return ok({
        code: '1',
        message: '',
        success: true,
        data: {
          list: [
            {
              attr: 'customerName',
              attrName: '客户名称',
              fieldType: 'TEXT',
              supportedSymbols: ['CONTAINS'],
              itemList: [],
            },
          ],
        },
      })
    }
    if (url.endsWith('/erp/v1/common/list/header')) return ok({ code: '1', message: '', success: true, data: { list: [] } })
    if (url.endsWith('/erp/v1/common/list/topButton')) return ok({ code: '1', message: '', success: true, data: { list: [] } })
    if (url.endsWith('/erp/v1/common/list/bottomButton')) return ok({ code: '1', message: '', success: true, data: { list: [] } })
    if (url.endsWith('/erp/v1/customer/list')) {
      expect(JSON.parse(String(init?.body))).toEqual({
        corpid: 'demo-corp',
        userId: '115014265324309213',
        pageNum: 1,
        pageSize: 20,
        keyword: '杭州',
        conditions: [{ attr: 'customerName', fieldType: 'TEXT', symbol: 'CONTAINS', value: ['客户'] }],
      })
      return ok({ code: '1', message: '', success: true, data: { headList: null, list: [], pageHelper: { page: 1, count: 1, hasLeft: false, hasRight: false } } })
    }
    throw new Error(`unexpected url: ${url}`)
  })

  const wrapper = mount(CustomerListPage)
  await flushPromises()
  await wrapper.get('[data-testid="filter-keyword"]').setValue('杭州')
  await wrapper.get('[data-testid="add-condition"]').trigger('click')
  await wrapper.get('[data-testid="condition-value-0-0"]').setValue('客户')
  await wrapper.get('.brand-btn[type="submit"]').trigger('submit')
  await flushPromises()
})
```

- [ ] **Step 2: 运行测试，确认页面还在拼旧的平铺字段请求体**

Run: `pnpm --filter @xbb-erp/admin-web test -- src/modules/customer/list/CustomerListPage.spec.ts`
Expected: FAIL，提示请求体仍包含 `customerCode`、`customerName` 或页面仍依赖 `queryForm`。

- [ ] **Step 3: 写最小实现，让客户列表页和模板页都接入公共动态筛选组件**

```ts
const keyword = ref('')
const conditions = ref<ListFilterCondition[]>([])

async function loadList(page = 1) {
  const payload = await postJson<CustomerListResponse>('/erp/v1/customer/list', {
    corpid: businessContext.corpid,
    userId: businessContext.userId,
    pageNum: page,
    pageSize: 20,
    keyword: keyword.value,
    conditions: conditions.value,
  })

  rows.value = payload.list ?? []
  pageHelper.value = payload.pageHelper
}

async function handleReset() {
  keyword.value = ''
  conditions.value = []
  await loadList(1)
}
```

```vue
<ListFilterPanel
  :filters="filters"
  :keyword="keyword"
  :conditions="conditions"
  @update:keyword="keyword = $event"
  @update:conditions="conditions = $event"
  @submit="handleSubmit"
  @reset="handleReset"
/>
```

```ts
const keyword = ref('')
const conditions = ref<ListFilterCondition[]>([])

async function loadList(page = 1) {
  const payload = await postJson('/erp/v1/replace/list', {
    corpid: businessContext.corpid,
    userId: businessContext.userId,
    pageNum: page,
    pageSize: 20,
    keyword: keyword.value,
    conditions: conditions.value,
  }) as TemplateListResponse

  rows.value = payload.list ?? []
  pageHelper.value = payload.pageHelper
}
```

- [ ] **Step 4: 再跑测试，确认客户列表页已完全切换协议**

Run: `pnpm --filter @xbb-erp/admin-web test -- src/modules/customer/list/CustomerListPage.spec.ts`
Expected: PASS，页面只发送 `keyword + conditions + 分页基础字段`，且模板页示例也与新公共组件契约一致。

- [ ] **Step 5: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/template/list/ListPageTemplate.vue

git commit -m "feat: wire customer list to dynamic filter protocol"
```

### Task 5: 更新接口文档并完成整体验证

**Files:**
- Modify: `docs/api/common-list.md`
- Modify: `docs/api/customer-customer.md`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/docs/package/list-page-components.md`
- Modify: `docs/kn/common-list-s.md`
- Modify: `docs/kn/common-m.md`

**Interfaces:**
- Consumes: `/erp/v1/common/list/filter` response fields `attr`, `attrName`, `fieldType`, `supportedSymbols`, `itemList`
- Consumes: `/erp/v1/customer/list` request body `corpid`, `userId`, `pageNum`, `pageSize`, `keyword`, `conditions`
- Produces: 文档与代码契约一致的 API 说明与前端组件接入说明

- [ ] **Step 1: 先补失败验证，确保文档前的代码状态已经完整可测**

```bash
mvn -pl xbb-erp-module-common,xbb-erp-module-customer \
  -Dtest=ListCommonServiceTest,ListFilterConditionBuilderTest,CustomerListMetaProviderTest,CustomerListServiceTest,ConditionMapHelperTest test
```

```bash
pnpm --filter @xbb-erp/admin-web test -- \
  src/components/list/ListFilterPanel.spec.ts \
  src/modules/customer/list/CustomerListPage.spec.ts
```

Expected: 两组命令全部 PASS；如果任一命令失败，先回到对应任务修复，再继续文档步骤。

- [ ] **Step 2: 更新接口与组件说明文档**

```md
`docs/api/common-list.md`：把 `FilterField` 改为 `attr / attrName / fieldType / supportedSymbols / itemList`。
`docs/api/customer-customer.md`：把客户列表请求体改为 `keyword + conditions + pageNum + pageSize + corpid + userId`，删除旧平铺筛选字段示例。
`/Users/bomfish/xbb-ai-erp-v2-front/docs/package/list-page-components.md`：将 `queryForm` 输入输出说明替换为 `keyword`、`conditions`、`ListFilterCondition`。
`docs/kn/common-list-s.md` 与 `docs/kn/common-m.md`：同步 common list filter 契约说明。
```

- [ ] **Step 3: 执行 `gen-api-md` 技能并检查文档 diff**

Run skill: `gen-api-md`
Expected: `docs/api/common-list.md` 与 `docs/api/customer-customer.md` 中的字段名、示例请求体、入参说明与当前代码一致；若技能生成的文档与手工修改冲突，以代码契约为准重新整理。

- [ ] **Step 4: 做最终回归验证**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonServiceTest,ListFilterConditionBuilderTest,CustomerListMetaProviderTest,CustomerListServiceTest,ConditionMapHelperTest test`
Expected: `BUILD SUCCESS`

Run: `pnpm --filter @xbb-erp/admin-web test -- src/components/list/ListFilterPanel.spec.ts src/modules/customer/list/CustomerListPage.spec.ts`
Expected: `2 passed` 或等价的全部通过输出

- [ ] **Step 5: 提交最终文档与验证结果**

```bash
git add \
  docs/api/common-list.md \
  docs/api/customer-customer.md \
  docs/kn/common-list-s.md \
  docs/kn/common-m.md \
  /Users/bomfish/xbb-ai-erp-v2-front/docs/package/list-page-components.md

git commit -m "docs: update dynamic filter API contracts"
```
