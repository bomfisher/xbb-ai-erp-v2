# Task 5 Brief

## 任务定位
把 `CustomerCreateDrawer` 从纯 `create` 模式演进为 `create / edit` 双模式，并在 `customerCreate.ts` 增加编辑初始化请求能力，为后续 Task 6 的列表页行内编辑编排做准备。

## Global Constraints
- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 用户已明确：不创建worktree，不允许提交，使用子代理模式。
- 客户编辑复用现有新建侧开抽屉，统一演进为 `create / edit` 双模式。
- 本任务只做抽屉与请求层，不接 `/rowAction` 页面编排；那是 Task 6 的范围。
- 只允许修改本任务列出的三个文件。
- 完成开发任务并通过验证后，执行 `gen-api-md` 规则；若没有接口变更，在报告中明确说明无需更新 `docs/api`。

## Ambiguity Resolution
- 原计划示例测试里出现“点击行内 EDIT 打开编辑抽屉”，但这依赖 Task 6 的 `CustomerListPage.vue` 页面编排。
- 本 Task 5 不允许修改 `CustomerListPage.vue`，所以这里明确：Task 5 只要求把抽屉本身做成双模式，并在测试里验证 `mode='edit'` + `customerId` 会走 `/erp/v1/customer/updateItem` 且显示“客户编辑”。
- 不要因为追求 page-level click path 而越界修改 `CustomerListPage.vue`。

## Files
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/customerCreate.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerCreateDrawer.vue`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts`

## Existing Context
- `customerCreate.ts` 当前只有 `fetchCustomerCreatePayload()`，调用 `/erp/v1/customer/addItem`。
- `CustomerCreateDrawer.vue` 当前 props 只有 `{ open: boolean }`，内部只会调用 `loadCreatePayload()`。
- `CustomerListPage.vue` 当前仍只接 `ADD` 打开抽屉，Task 6 才会接 `rowAction` 和 `EDIT` 编排。

## Interfaces
- Consumes: `POST /erp/v1/customer/addItem` and `POST /erp/v1/customer/updateItem`.
- Produces:
  - `type CustomerDrawerMode = 'create' | 'edit'`
  - `fetchCustomerUpdatePayload(id: number | string): Promise<SaveItemPayload>`
  - `CustomerCreateDrawer` props `{ open: boolean; mode: CustomerDrawerMode; customerId?: number | string }`

## Required Test Additions
在 `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/modules/customer/list/CustomerListPage.spec.ts` 新增或补充一个聚焦双模式的测试。允许直接 `mount(CustomerCreateDrawer, ...)`，不要为此去改页面编排。

```ts
it('loads edit payload when drawer opens in edit mode', async () => {
  fetchMock.mockImplementation(async (input: RequestInfo | URL) => {
    const url = String(input)
    if (url.endsWith('/erp/v1/customer/updateItem')) {
      return ok({ code: '1', message: '', success: true, data: {
        headList: [{ attr: 'main.customerName', attrName: '客户名称', fieldType: '1', required: 1, editable: 1, itemList: [] }],
        data: {
          main: { id: 1, customerName: '杭州客户' },
          contacts: [],
          addresses: [],
          bankAccounts: [],
          invoiceProfiles: [],
          sectionState: { contacts: 0, addresses: 0, bankAccounts: 0, invoiceProfiles: 0 },
        },
      } })
    }
    throw new Error(`unexpected url: ${url}`)
  })

  const wrapper = mount(CustomerCreateDrawer, {
    props: {
      open: true,
      mode: 'edit',
      customerId: 1,
    },
  })
  await flushPromises()

  expect(wrapper.text()).toContain('客户编辑')
  expect(wrapper.get('input[data-field="main.customerName"]').element).toHaveProperty('value', '杭州客户')
  expect(fetchMock).toHaveBeenCalledWith(
    '/erp/v1/customer/updateItem',
    expect.objectContaining({ method: 'POST' }),
  )
})
```

可再补一个 create 模式回归断言，确保 `mode='create'` 仍走 `/erp/v1/customer/addItem`。

## Required Commands
- Failing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`
- Passing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/modules/customer/list/CustomerListPage.spec.ts`

## Required Implementation Snippet
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

并把模板中的标题/描述改为使用 `drawerTitle` / `drawerDescription`。

## Additional Execution Rules
- 必须先写失败测试，再实现最小代码，再回归目标测试。
- 不要修改 `CustomerListPage.vue`；Task 6 才处理页面 `rowAction` 与 `EDIT` 编排。
- 保持草稿、正式保存、草稿列表、toast 等现有行为不回归。
- 不要创建 git commit。
- 完成后把完整结果写入：`/Users/bomfish/xbb-ai-erp-v2/docs/superpowers/plans/2026-07-29-customer-list-row-edit-task-5-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
