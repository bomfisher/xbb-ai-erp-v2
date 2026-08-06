# Task 6 Brief

## 任务定位
在 `CustomerListPage.vue` 接通后端 `/erp/v1/common/list/rowAction` 元数据、把 `rowActions` 传给 `ListDataTable`，并在页面层完成 `ADD` / `EDIT` 两种抽屉打开编排。

## Global Constraints
- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 用户已明确：不创建worktree，不允许提交，使用子代理模式。
- 不做前端本地硬编码权限判断；动作集合由后端元数据下发。
- 行内操作列放在表格最后一列，并固定在右侧。
- 行内动作统一采用“主动作直出，其余进入更多”的形态。
- 客户编辑复用现有新建侧开抽屉，统一演进为 `create / edit` 双模式。
- 本次只落地客户 `EDIT`，不提前接入删除、停用、提交等其他动作。
- 只允许修改本任务列出的两个文件。
- 完成开发任务并通过验证后，执行 `gen-api-md` 规则；若没有接口变更，在报告中明确说明无需更新 `docs/api`。

## Files
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.vue`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts`

## Existing Context
- `ListDataTable.vue` 已支持可选 `rowActions?: ListRowActionItem[]`，并会透传 `row-action` 事件。
- `CustomerCreateDrawer.vue` 已支持 props `{ open, mode, customerId }`，其中 `mode` 默认 `create`。
- `types.ts` 已有 `ListRowActionItem` 类型。
- `CustomerListPage.vue` 当前仍只并行请求 4 个元数据接口 + 1 个列表接口，总计 5 次请求，且只处理 `ADD` 打开抽屉。

## Interfaces
- Consumes:
  - `ListDataTable` prop `rowActions?: ListRowActionItem[]`
  - `ListDataTable` emit `row-action`
  - `CustomerCreateDrawer` props `mode`, `customerId`
  - `POST /erp/v1/common/list/rowAction`
- Produces:
  - page state `rowActions`, `drawerMode`, `editingCustomerId`
  - handler `handleRowAction(payload: { actionCode: string; row: CustomerListItem })`

## Required Test Additions
在 `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts` 基于现有页面测试扩写：

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

至少需要覆盖两个重点：
1. 页面初始化时会额外请求 `/erp/v1/common/list/rowAction`，总请求数从 5 变 6。
2. 点击行内 `EDIT` 后，页面会以 `mode='edit'` 与正确 `customerId` 打开 `CustomerCreateDrawer`，从而触发 `/erp/v1/customer/updateItem`。

## Required Commands
- Failing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
- Passing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`

## Required Implementation Snippet
```ts
const rowActions = ref<ListRowActionItem[]>([])
const drawerMode = ref<CustomerDrawerMode>('create')
const editingCustomerId = ref<number | string | undefined>(undefined)
```

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

## Additional Execution Rules
- 必须先写失败测试，再实现最小代码，再回归目标测试。
- 只改 `CustomerListPage.vue` 和 `CustomerListPage.spec.ts`，不要回头修改 `CustomerCreateDrawer.vue`、`customerCreate.ts`、`ListDataTable.vue`。
- 如果需要排序行内动作，复用按钮排序思路新增最小 `sortRowActions()`，不要引入额外抽象。
- 不要创建 git commit。
- 完成后把完整结果写入：`/Users/bomfish/xbb-ai-erp-v2/docs/superpowers/plans/2026-07-29-customer-list-row-edit-task-6-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
