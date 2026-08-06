# 客户列表行内编辑与通用操作列 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为客户列表接通右侧固定行内操作列和编辑抽屉复用链路，并沉淀可复用到其他单据页的通用操作列底层。

**Architecture:** 后端在现有列表元数据体系中新增 `rowAction` 元数据出口，客户列表首版只下发 `EDIT` 主动作；前端在 `ListDataTable` 上补齐右侧固定操作列能力，并新增独立 `ListRowActions` 组件承接“主动作直出 + 更多菜单”渲染。客户列表页继续负责业务编排，复用现有 `CustomerCreateDrawer`，将其演进为 `create / edit` 双模式抽屉，通过 `addItem` / `updateItem` 区分初始化链路。

**Tech Stack:** Java 21、Spring Boot 3.3.2、Maven、JUnit 5；Vue 3、TypeScript、Vitest。

## Global Constraints

- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 所有主动捕获的报错、业务的主动抛错，都使用 `BizException`。
- 非脚本接口入参 DTO 统一继承 `BaseDTO`，接口参数返回统一使用 `ResultVO.success()` 包装。
- `userId` 员工 Id 是字符串 id。
- getter / setter 用 Lombok 管理。
- 不做前端本地硬编码权限判断；动作集合由后端元数据下发。
- 行内操作列放在表格最后一列，并固定在右侧。
- 行内动作统一采用“主动作直出，其余进入更多”的形态。
- 客户编辑复用现有新建侧开抽屉，统一演进为 `create / edit` 双模式。
- 本次只落地客户 `EDIT`，不提前接入删除、停用、提交等其他动作。
- 完成开发任务并通过验证后，执行技能 `gen-api-md` 更新接口文档。

---

## File Map

- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java` — 列表元数据提供者接口，补充 `rowAction` 出口。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java` — 列表元数据聚合对象，新增 `rowActionList`。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java` — 公共列表元数据控制器，新增 `/rowAction` 接口。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java` — 公共列表元数据服务接口，新增 `rowAction()`。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java` — 列表元数据服务实现，打通 provider -> VO 输出。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java` — 新增行内动作 VO。
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java` — 新增行内动作元数据项。
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java` — 补 `/rowAction` 结构测试。
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java` — 补服务调度和契约测试。
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java` — 客户列表元数据提供者，补 `EDIT` 行内动作下发。
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java` — 验证客户 `rowAction` 元数据。
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java` — 如有需要，校验构造器与接口实现保持稳定。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts` — 新增 `ListRowActionItem`、`ListRowActionShowMode` 等前端类型。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.vue` — 新增通用行内操作组件。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.vue` — 新增右侧固定操作列与 `row-action` 事件能力。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/customerCreate.ts` — 补编辑初始化请求与抽屉模式载荷转换。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerCreateDrawer.vue` — 演进为 `create / edit` 双模式抽屉。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue` — 拉取 `rowAction` 元数据、监听行内动作并打开编辑抽屉。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts` — 补客户列表行内编辑链路测试。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.spec.ts` — 新增表格操作列行为测试。
- `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.spec.ts` — 新增行内动作组件测试。
- `docs/api/common-list.md` — 补 `/erp/v1/common/list/rowAction` 文档。
- `docs/api/customer-customer.md` — 补客户列表编辑初始化链路说明（如果本次接口行为或文档结构需调整）。

### Task 1: 打通后端 rowAction 元数据协议

**Files:**
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

**Interfaces:**
- Consumes: `ListMetaProvider#buildTopButtonMeta(ListCommonQueryDTO)`、`ListMetaProvider#buildBottomButtonMeta(ListCommonQueryDTO)` 现有元数据装配模式。
- Produces:
  - `ListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto): ListMetaBundlePojo`
  - `ListCommonService#rowAction(ListCommonQueryDTO dto): ListRowActionVO`
  - `ListRowActionVO#getList(): List<ListRowActionItemPojo>`
  - `ListMetaBundlePojo#setRowActionList(List<ListRowActionItemPojo>)`

- [ ] **Step 1: 写后端失败测试，锁定 `/rowAction` 契约**

```java
@Test
void should_dispatch_row_action_meta_by_business_code() {
    ListMetaProvider provider = new StubListMetaProvider();
    ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
    ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setBusinessCode("CUSTOMER");
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");

    ListRowActionVO rowActionVO = service.rowAction(dto);

    assertEquals("EDIT", rowActionVO.getList().get(0).getActionCode());
    assertEquals("编辑", rowActionVO.getList().get(0).getActionName());
    assertEquals("PRIMARY", rowActionVO.getList().get(0).getShowMode());
}

@Test
void should_define_row_action_endpoint_on_common_controller() throws Exception {
    Method rowAction = ListCommonController.class.getMethod("rowAction", ListCommonQueryDTO.class);
    assertNotNull(rowAction);
}
```

- [ ] **Step 2: 运行测试，确认当前失败**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
Expected: FAIL，提示 `rowAction` 方法、`ListRowActionVO` 或 `buildRowActionMeta` 尚不存在。

- [ ] **Step 3: 先补最小 POJO/VO 与 provider/service/controller 接口**

```java
@Data
public class ListRowActionItemPojo {
    private String actionCode;
    private String actionName;
    private Integer sort;
    private String showMode;
    private String confirmType;
}
```

```java
@Data
public class ListRowActionVO {
    private List<ListRowActionItemPojo> list;
}
```

```java
public interface ListMetaProvider {
    String businessCode();
    List<FilterField> buildFilterMeta(ListCommonQueryDTO dto);
    List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto);

    default void applyPackageExtension(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }

    default void applyPermissionTrim(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }
}
```

```java
@Data
public class ListMetaBundlePojo {
    private List<FilterField> filterList;
    private List<ListButtonItemPojo> topButtonList;
    private List<ListButtonItemPojo> bottomButtonList;
    private List<ListRowActionItemPojo> rowActionList;
}
```

```java
@PostMapping("/rowAction")
public ResultVO<ListRowActionVO> rowAction(@RequestBody ListCommonQueryDTO dto) {
    return ResultVO.success(listCommonService.rowAction(dto));
}
```

- [ ] **Step 4: 实现 service 组装逻辑，复用现有 bundle 输出模式**

```java
@Override
public ListRowActionVO rowAction(ListCommonQueryDTO dto) {
    ListMetaProvider provider = registry.get(dto.getBusinessCode());
    ListMetaBundlePojo bundle = provider.buildRowActionMeta(dto);
    ListRowActionVO vo = new ListRowActionVO();
    vo.setList(bundle.getRowActionList());
    return vo;
}
```

```java
@Override
public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
    return new ListMetaBundlePojo();
}
```

- [ ] **Step 5: 运行测试，确认后端公共元数据契约通过**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
Expected: PASS，`/rowAction` 控制器结构与服务调度测试通过。

- [ ] **Step 6: 提交这一小步**

```bash
git add \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java

git commit -m "feat: add common list row action meta"
```

### Task 2: 客户列表首版下发 EDIT 行内动作

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java`

**Interfaces:**
- Consumes: `ListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto)` from Task 1。
- Produces:
  - `CustomerListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto): ListMetaBundlePojo`
  - `ListMetaBundlePojo#getRowActionList(): List<ListRowActionItemPojo>` returning customer `EDIT` action

- [ ] **Step 1: 写客户 provider 失败测试，锁定 `EDIT` 元数据输出**

```java
@Test
void should_build_customer_row_action_meta() {
    CustomerListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

    ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);

    assertEquals(1, rowActionBundle.getRowActionList().size());
    assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
    assertEquals("编辑", rowActionBundle.getRowActionList().get(0).getActionName());
    assertEquals("PRIMARY", rowActionBundle.getRowActionList().get(0).getShowMode());
}
```

- [ ] **Step 2: 运行客户 provider 测试，确认当前失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`
Expected: FAIL，提示 `buildRowActionMeta` 未实现或 `rowActionList` 为空。

- [ ] **Step 3: 在客户 provider 中补最小 EDIT 元数据**

```java
@Override
public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
    ListMetaBundlePojo bundle = new ListMetaBundlePojo();
    bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
    return bundle;
}

private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
    ListRowActionItemPojo item = new ListRowActionItemPojo();
    item.setActionCode(actionCode);
    item.setActionName(actionName);
    item.setSort(sort);
    item.setShowMode(showMode);
    item.setConfirmType(confirmType);
    return item;
}
```

- [ ] **Step 4: 运行客户 provider 测试，确认元数据输出稳定**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`
Expected: PASS，客户 `EDIT` 行内动作测试通过，现有 filter/header/topButton/bottomButton 断言不回归。

- [ ] **Step 5: 提交这一小步**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java

git commit -m "feat: expose customer row edit action meta"
```

### Task 3: 前端新增通用行内操作组件

**Files:**
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.vue`
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.spec.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts`

**Interfaces:**
- Consumes: backend `/erp/v1/common/list/rowAction` response item fields `actionCode`, `actionName`, `sort`, `showMode`, `confirmType`。
- Produces:
  - `type ListRowActionItem = { actionCode: string; actionName: string; sort: number; showMode: 'PRIMARY' | 'MORE'; confirmType?: string }`
  - `ListRowActions` props `{ row: ListRow; actions: ListRowActionItem[] }`
  - `ListRowActions` emits `action` with payload `{ actionCode: string; row: ListRow }`

- [ ] **Step 1: 写组件失败测试，锁定“直出主动作 + 更多菜单”行为**

```ts
it('renders primary action and emits row payload', async () => {
  const wrapper = mount(ListRowActions, {
    props: {
      row: { id: 1, customerCode: 'CUST-001' },
      actions: [
        { actionCode: 'EDIT', actionName: '编辑', sort: 10, showMode: 'PRIMARY' },
      ],
    },
  })

  expect(wrapper.text()).toContain('编辑')
  await wrapper.get('[data-row-action="EDIT"]').trigger('click')
  expect(wrapper.emitted('action')).toEqual([[{ actionCode: 'EDIT', row: { id: 1, customerCode: 'CUST-001' } }]])
})

it('moves non-primary actions into more menu', async () => {
  const wrapper = mount(ListRowActions, {
    props: {
      row: { id: 1 },
      actions: [
        { actionCode: 'EDIT', actionName: '编辑', sort: 10, showMode: 'PRIMARY' },
        { actionCode: 'DELETE', actionName: '删除', sort: 20, showMode: 'MORE' },
      ],
    },
  })

  expect(wrapper.text()).toContain('编辑')
  expect(wrapper.text()).toContain('更多')
})
```

- [ ] **Step 2: 运行组件测试，确认当前失败**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts`
Expected: FAIL，报错指向 `ListRowActions.vue` 或 `ListRowActionItem` 类型尚不存在。

- [ ] **Step 3: 在 `types.ts` 中补齐类型定义**

```ts
export type ListRowActionShowMode = 'PRIMARY' | 'MORE'

export type ListRowActionItem = {
  actionCode: string
  actionName: string
  sort: number
  showMode: ListRowActionShowMode
  confirmType?: string
}
```

- [ ] **Step 4: 实现 `ListRowActions.vue` 最小可用版本**

```vue
<script setup lang="ts">
import { computed } from 'vue'
import type { ListRowActionItem } from './types'

type ListRow = {
  id: number | string
  [key: string]: unknown
}

const props = defineProps<{
  row: ListRow
  actions: ListRowActionItem[]
}>()

const emit = defineEmits<{
  action: [{ actionCode: string; row: ListRow }]
}>()

const sortedActions = computed(() => [...props.actions].sort((left, right) => left.sort - right.sort))
const primaryActions = computed(() => sortedActions.value.filter(item => item.showMode === 'PRIMARY'))
const moreActions = computed(() => sortedActions.value.filter(item => item.showMode !== 'PRIMARY'))

function emitAction(actionCode: string) {
  emit('action', { actionCode, row: props.row })
}
</script>

<template>
  <div class="list-row-actions">
    <button
      v-for="item in primaryActions"
      :key="item.actionCode"
      :data-row-action="item.actionCode"
      class="ghost-btn ghost-btn--sm"
      type="button"
      @click="emitAction(item.actionCode)"
    >
      {{ item.actionName }}
    </button>
    <details v-if="moreActions.length > 0" class="list-row-actions__more">
      <summary>更多</summary>
      <div class="list-row-actions__menu">
        <button
          v-for="item in moreActions"
          :key="item.actionCode"
          :data-row-action="item.actionCode"
          class="list-row-actions__menu-item"
          type="button"
          @click="emitAction(item.actionCode)"
        >
          {{ item.actionName }}
        </button>
      </div>
    </details>
  </div>
</template>
```

- [ ] **Step 5: 运行组件测试，确认行内动作组件通过**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts`
Expected: PASS，`EDIT` 直出和 `更多` 菜单断言通过。

- [ ] **Step 6: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.spec.ts

git commit -m "feat: add reusable row action component"
```

### Task 4: 为 ListDataTable 增加右侧固定操作列能力

**Files:**
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.vue`
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.spec.ts`
- Consumes: `ListRowActions` from Task 3。

**Interfaces:**
- Consumes: `ListRowActions` props `{ row, actions }` and emits `action`。
- Produces:
  - `ListDataTable` new optional prop `rowActions?: ListRowActionItem[]`
  - `ListDataTable` emits `row-action` with payload `{ actionCode: string; row: ListRow }`
  - right-fixed operation column with header text `操作`

- [ ] **Step 1: 写表格失败测试，锁定操作列渲染与事件透传**

```ts
it('renders fixed operation column when rowActions exist', () => {
  const wrapper = mount(ListDataTable, {
    props: {
      title: '客户数据',
      description: '测试表格',
      headers: [{ attr: 'main.customerCode', attrName: '客户编码', fieldType: '1', required: 0, editable: 1, itemList: [] }],
      rows: [{ id: 1, customerCode: 'CUST-001' }],
      renderCell: (row, attr) => attr === 'main.customerCode' ? String(row.customerCode ?? '') : '—',
      rowActions: [{ actionCode: 'EDIT', actionName: '编辑', sort: 10, showMode: 'PRIMARY' }],
    },
  })

  expect(wrapper.text()).toContain('操作')
  expect(wrapper.get('th[data-column="row-actions"]').classes()).toContain('data-table__cell--sticky-right')
})

it('re-emits row action event from row actions component', async () => {
  const wrapper = mount(ListDataTable, {
    props: {
      title: '客户数据',
      description: '测试表格',
      headers: [{ attr: 'main.customerCode', attrName: '客户编码', fieldType: '1', required: 0, editable: 1, itemList: [] }],
      rows: [{ id: 1, customerCode: 'CUST-001' }],
      renderCell: (row, attr) => attr === 'main.customerCode' ? String(row.customerCode ?? '') : '—',
      rowActions: [{ actionCode: 'EDIT', actionName: '编辑', sort: 10, showMode: 'PRIMARY' }],
    },
  })

  await wrapper.get('[data-row-action="EDIT"]').trigger('click')
  expect(wrapper.emitted('row-action')).toEqual([[{ actionCode: 'EDIT', row: { id: 1, customerCode: 'CUST-001' } }]])
})
```

- [ ] **Step 2: 运行表格测试，确认当前失败**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListDataTable.spec.ts`
Expected: FAIL，提示 `rowActions` prop、`row-action` 事件或 sticky 列不存在。

- [ ] **Step 3: 在 `ListDataTable.vue` 中接入 `ListRowActions` 并补 prop / emit**

```vue
<script setup lang="ts">
import { computed } from 'vue'
import ListRowActions from './ListRowActions.vue'
import type { HeaderField, ListRowActionItem } from './types'

type ListRow = {
  id: number | string
  [key: string]: unknown
}

const props = defineProps<{
  title: string
  description: string
  headers: HeaderField[]
  rows: ListRow[]
  renderCell: (row: ListRow, attr: string) => string
  rowActions?: ListRowActionItem[]
}>()

const emit = defineEmits<{
  'row-action': [{ actionCode: string; row: ListRow }]
}>()

const visibleHeaders = computed(() => props.headers)
const hasRowActions = computed(() => (props.rowActions ?? []).length > 0)

function handleRowAction(payload: { actionCode: string; row: ListRow }) {
  emit('row-action', payload)
}
</script>
```

- [ ] **Step 4: 在表格模板中补最后一列 `操作` 与右侧固定类名**

```vue
<thead>
  <tr>
    <th v-for="header in visibleHeaders" :key="header.attr">{{ header.attrName }}</th>
    <th v-if="hasRowActions" data-column="row-actions" class="data-table__cell--sticky-right">操作</th>
  </tr>
</thead>
<tbody>
  <tr v-if="rows.length === 0">
    <td :colspan="Math.max(visibleHeaders.length + (hasRowActions ? 1 : 0), 1)" class="customer-list-page__empty-cell">暂无数据</td>
  </tr>
  <tr v-for="row in rows" :key="row.id">
    <td v-for="header in visibleHeaders" :key="`${row.id}-${header.attr}`">{{ renderCell(row, header.attr) }}</td>
    <td v-if="hasRowActions" class="data-table__cell--sticky-right">
      <ListRowActions :row="row" :actions="rowActions ?? []" @action="handleRowAction" />
    </td>
  </tr>
</tbody>
```

- [ ] **Step 5: 运行表格与行内动作测试，确认底层能力通过**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts src/components/list/ListDataTable.spec.ts`
Expected: PASS，操作列渲染、右侧固定类名和事件透传测试通过。

- [ ] **Step 6: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListDataTable.spec.ts

git commit -m "feat: support sticky row actions in list table"
```

### Task 5: 客户抽屉演进为 create / edit 双模式

**Files:**
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/customerCreate.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerCreateDrawer.vue`
- Test: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts`

**Interfaces:**
- Consumes: `POST /erp/v1/customer/addItem` and `POST /erp/v1/customer/updateItem`。
- Produces:
  - `type CustomerDrawerMode = 'create' | 'edit'`
  - `fetchCustomerUpdatePayload(id: number | string): Promise<SaveItemPayload>`
  - `CustomerCreateDrawer` props `{ open: boolean; mode: CustomerDrawerMode; customerId?: number | string }`

- [ ] **Step 1: 先写客户列表失败测试，锁定编辑抽屉初始化链路**

```ts
it('opens edit drawer and requests updateItem after clicking row edit', async () => {
  fetchMock.mockImplementation(async (input: RequestInfo | URL) => {
    const url = String(input)
    if (url.endsWith('/erp/v1/common/list/rowAction')) {
      return ok({ code: '1', message: '', success: true, data: { list: [{ actionCode: 'EDIT', actionName: '编辑', sort: 10, showMode: 'PRIMARY', confirmType: 'NONE' }] } })
    }
    if (url.endsWith('/erp/v1/customer/updateItem')) {
      return ok({ code: '1', message: '', success: true, data: {
        headList: [{ attr: 'main.customerName', attrName: '客户名称', fieldType: '1', required: 1, editable: 1, itemList: [] }],
        data: { main: { id: 1, customerName: '杭州客户' }, contacts: [], addresses: [], bankAccounts: [], invoiceProfiles: [], sectionState: { contacts: 0, addresses: 0, bankAccounts: 0, invoiceProfiles: 0 } },
      } })
    }
    return existingMock(url)
  })

  const wrapper = mount(CustomerListPage)
  await flushPromises()
  await wrapper.get('[data-row-action="EDIT"]').trigger('click')
  await flushPromises()

  expect(wrapper.text()).toContain('客户编辑')
  expect(wrapper.get('input[data-field="main.customerName"]').element).toHaveProperty('value', '杭州客户')
})
```

- [ ] **Step 2: 运行客户列表测试，确认当前失败**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
Expected: FAIL，提示 `/rowAction` 未请求、`EDIT` 按钮未出现或 `updateItem` 链路不存在。

- [ ] **Step 3: 在 `customerCreate.ts` 中补编辑初始化请求**

```ts
export type CustomerDrawerMode = 'create' | 'edit'

export async function fetchCustomerCreatePayload(): Promise<SaveItemPayload> {
  return postCustomerJson<SaveItemPayload>('/erp/v1/customer/addItem', customerBusinessContext)
}

export async function fetchCustomerUpdatePayload(id: number | string): Promise<SaveItemPayload> {
  return postCustomerJson<SaveItemPayload>('/erp/v1/customer/updateItem', {
    ...customerBusinessContext,
    id,
  })
}
```

- [ ] **Step 4: 将 `CustomerCreateDrawer.vue` 改造成双模式加载**

```ts
const props = defineProps<{
  open: boolean
  mode: CustomerDrawerMode
  customerId?: number | string
}>()

const drawerTitle = computed(() => props.mode === 'edit' ? '客户编辑' : '客户新建')
const drawerDescription = computed(() => props.mode === 'edit' ? '侧开抽屉承载客户编辑表单。' : '侧开抽屉承载客户新建表单。')

async function loadPayload() {
  loading.value = true
  loadErrorMessage.value = ''
  hideActionToast()
  payload.value = null
  draftMeta.value = {}

  try {
    payload.value = props.mode === 'edit'
      ? await fetchCustomerUpdatePayload(String(props.customerId ?? ''))
      : await fetchCustomerCreatePayload()
    formData.value = normalizeFormData(payload.value.data)
  } catch (error) {
    loadErrorMessage.value = error instanceof Error ? error.message : (props.mode === 'edit' ? '客户编辑页加载失败' : '客户新建页加载失败')
  } finally {
    loading.value = false
  }
}
```

- [ ] **Step 5: 运行客户列表测试，确认抽屉双模式行为通过**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
Expected: PASS，新增 `EDIT` 行内入口后，抽屉可进入编辑态并调用 `updateItem`。

- [ ] **Step 6: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/customerCreate.ts \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerCreateDrawer.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts

git commit -m "feat: support edit mode in customer drawer"
```

### Task 6: 客户列表页接通 rowAction 元数据与编辑事件编排

**Files:**
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue`
- Test: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts`

**Interfaces:**
- Consumes:
  - `ListDataTable` prop `rowActions?: ListRowActionItem[]`
  - `ListDataTable` emit `row-action`
  - `CustomerCreateDrawer` props `mode`, `customerId`
  - `POST /erp/v1/common/list/rowAction`
- Produces:
  - page state `rowActions`, `drawerMode`, `editingCustomerId`
  - handler `handleRowAction(payload: { actionCode: string; row: CustomerListItem })`

- [ ] **Step 1: 扩写失败测试，锁定列表页完整加载 6 个请求与编辑编排**

```ts
expect(fetchMock).toHaveBeenCalledWith(
  '/erp/v1/common/list/rowAction',
  expect.objectContaining({ method: 'POST' }),
)
expect(wrapper.find('th[data-column="row-actions"]').text()).toContain('操作')
await wrapper.get('[data-row-action="EDIT"]').trigger('click')
await flushPromises()
expect(wrapper.text()).toContain('客户编辑')
```

- [ ] **Step 2: 运行客户列表测试，确认当前失败**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
Expected: FAIL，提示页面未请求 `/rowAction` 或未把 `rowActions` 传给表格。

- [ ] **Step 3: 在页面状态中补 `rowActions`、`drawerMode`、`editingCustomerId`**

```ts
const rowActions = ref<ListRowActionItem[]>([])
const drawerMode = ref<CustomerDrawerMode>('create')
const editingCustomerId = ref<number | string | undefined>(undefined)
```

- [ ] **Step 4: 在 `onMounted()` 中并行拉取 `/rowAction` 并接通表格与抽屉**

```ts
const [filterPayload, headerPayload, topButtonPayload, bottomButtonPayload, rowActionPayload] = await Promise.all([
  postJson<ListPayload<FilterField>>('/erp/v1/common/list/filter', businessContext),
  postJson<ListPayload<HeaderField>>('/erp/v1/common/list/header', businessContext),
  postJson<ListPayload<ListButtonItem>>('/erp/v1/common/list/topButton', businessContext),
  postJson<ListPayload<ListButtonItem>>('/erp/v1/common/list/bottomButton', businessContext),
  postJson<ListPayload<ListRowActionItem>>('/erp/v1/common/list/rowAction', businessContext),
])

rowActions.value = sortRowActions(rowActionPayload.list ?? [])
```

```ts
function handleHeaderAction(button: ListButtonItem) {
  if (button.buttonCode === 'ADD' || button.actionCode === 'ADD') {
    drawerMode.value = 'create'
    editingCustomerId.value = undefined
    createDrawerOpen.value = true
  }
}

function handleRowAction(payload: { actionCode: string; row: CustomerListItem }) {
  if (payload.actionCode === 'EDIT') {
    drawerMode.value = 'edit'
    editingCustomerId.value = payload.row.id
    createDrawerOpen.value = true
  }
}
```

```vue
<ListDataTable
  :headers="headers"
  :rows="rows"
  :render-cell="renderCell"
  :row-actions="rowActions"
  title="客户数据"
  description="当前按客户主档维度展示列表信息。"
  @row-action="handleRowAction"
>
```

```vue
<CustomerCreateDrawer
  :open="createDrawerOpen"
  :mode="drawerMode"
  :customer-id="editingCustomerId"
  @close="closeCreateDrawer"
  @saved="handleDrawerSaved"
/>
```

- [ ] **Step 5: 运行客户列表测试，确认列表页完整编排通过**

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
Expected: PASS，页面初始化改为 6 个元数据/列表请求，`EDIT` 行内入口能打开编辑抽屉。

- [ ] **Step 6: 提交这一小步**

```bash
git add \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue \
  /Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts

git commit -m "feat: wire customer row edit flow"
```

### Task 7: 更新接口文档并做整体验证

**Files:**
- Modify: `docs/api/common-list.md`
- Modify: `docs/api/customer-customer.md`

**Interfaces:**
- Consumes: `/erp/v1/common/list/rowAction` response structure from Task 1、customer `updateItem` behavior already exposed in controller。
- Produces:
  - `docs/api/common-list.md` section for `POST /erp/v1/common/list/rowAction`
  - refreshed API docs via `gen-api-md` skill after code verification

- [ ] **Step 1: 写最小文档变更，补 `/rowAction` 协议说明**

```md
## 行内动作元数据

- 请求 URL：`POST /erp/v1/common/list/rowAction`
- 入参：`ListCommonQueryDTO`
- 出参：`ResultVO<ListRowActionVO>`

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [
      {
        "actionCode": "EDIT",
        "actionName": "编辑",
        "sort": 10,
        "showMode": "PRIMARY",
        "confirmType": "NONE"
      }
    ]
  }
}
```
```

- [ ] **Step 2: 运行后端与前端目标测试，确认全部通过**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest,CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`
Expected: PASS，公共元数据和客户 provider 相关测试全部通过。

Run: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts src/components/list/ListDataTable.spec.ts src/modules/customer/list/CustomerListPage.spec.ts`
Expected: PASS，行内动作组件、表格底层、客户编辑编排测试全部通过。

- [ ] **Step 3: 执行 `gen-api-md` 技能更新接口文档**

Run skill: `gen-api-md`
Expected: 依据最终代码与接口协议，补齐或刷新客户与公共列表接口文档。

- [ ] **Step 4: 做最终自查，确认无 spec 漏项**

Checklist:
- 右侧固定操作列已落地
- 客户首版 `EDIT` 已由后端元数据下发
- 前端不再硬编码客户行内动作集合
- 抽屉已支持 `create / edit`
- 列表页点击 `编辑` 后走 `updateItem`
- 文档已同步

- [ ] **Step 5: 提交最终结果**

```bash
git add \
  docs/api/common-list.md \
  docs/api/customer-customer.md

git commit -m "docs: add row action and customer edit docs"
```

## Self-Review

- **Spec coverage:**
  - 操作列最后一列与右侧固定：Task 4
  - 主动作直出 + 更多：Task 3 / Task 4
  - 动作来源走后端元数据：Task 1 / Task 2 / Task 6
  - 客户复用抽屉进入编辑态：Task 5 / Task 6
  - 异常与测试验收：Task 3~7
  - 文档同步：Task 7
- **Placeholder scan:** 已检查，无 `TODO` / `TBD` / “自行处理” 类占位。
- **Type consistency:** 计划统一使用 `ListRowActionItemPojo`、`ListRowActionVO`、`ListRowActionItem`、`CustomerDrawerMode`、`row-action` 事件签名 `{ actionCode, row }`。
