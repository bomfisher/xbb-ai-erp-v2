# Task 3 Brief

## 任务定位
实现前端通用行内操作组件 `ListRowActions`，作为后续 `ListDataTable` 右侧固定操作列的基础渲染单元。这个任务只负责“主动作直出 + 更多菜单”的最小通用组件，不接客户业务编排。

## Global Constraints
- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 不做前端本地硬编码权限判断；动作集合由后端元数据下发。
- 行内操作列放在表格最后一列，并固定在右侧。
- 行内动作统一采用“主动作直出，其余进入更多”的形态。
- 客户编辑复用现有新建侧开抽屉，统一演进为 `create / edit` 双模式。
- 本次只落地客户 `EDIT`，不提前接入删除、停用、提交等其他动作。
- 用户明确不允许本次创建 git commit。

## Files
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.vue`
- Create: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/ListRowActions.spec.ts`
- Modify: `/Users/bomfish/xbb-ai-erp-v2-front/apps/admin-web/src/components/list/types.ts`

## Interfaces
- Consumes: backend `/erp/v1/common/list/rowAction` response item fields `actionCode`, `actionName`, `sort`, `showMode`, `confirmType`.
- Produces:
  - `type ListRowActionShowMode = 'PRIMARY' | 'MORE'`
  - `type ListRowActionItem = { actionCode: string; actionName: string; sort: number; showMode: 'PRIMARY' | 'MORE'; confirmType?: string }`
  - `ListRowActions` props `{ row: ListRow; actions: ListRowActionItem[] }`
  - `ListRowActions` emits `action` with payload `{ actionCode: string; row: ListRow }`

## Required Test Additions
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

## Required Commands
- Failing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts`
- Passing test: `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts`

## Required Implementation Snippet
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
```

## Additional Execution Rules
- 必须先写失败测试，再实现最小代码，再回归目标测试。
- 只修改列出的三个前端文件；不要提前修改 `ListDataTable.vue`、`CustomerListPage.vue`、`CustomerCreateDrawer.vue`。
- 如果需要少量样式，放在组件内最小范围内，不要做全局样式清理。
- 不要创建 git commit。
- 完成后把完整结果写入同目录报告文件 `2026-07-29-customer-list-row-edit-task-3-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
