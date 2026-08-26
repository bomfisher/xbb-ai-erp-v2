# 审批流程设置

审批设置页复用 ERP 现有 `/erp/v1/menu/list` 菜单接口；租户管理员配置菜单项 `APPROVAL_FLOW_MANAGE`，路由为 `/management/system/approval-flow`，组件为 `management/system/approval-flow`。

菜单接口只提供导航数据，不能自动注册前端路由。前端必须在 `apps/admin-web/src/router.ts` 显式注册该路径；页面组件完成前无需注册 `pageRegistry`，菜单工作区会显示“页面内容待后续接入”。根因和固定接入步骤见前端 `docs/package/menu-route-integration.md`。

页面通过 `/erp/v1/approval/flow/*` 管理新建、编辑两个审批场景下的未启用流程、启用版本和停用状态。新建时必须先从审批业务目录选择业务对象与场景，再基于该业务登记的字段白名单配置适用范围和条件分支。新建保存默认未启用；编辑按原流程 ID 保存，保留当前启用状态和版本。当前仅交付流程定义设置；审批实例、待办和具体业务单据接入将在指定试点业务后交付。

- 列表：`docs/api/endpoints/approval-flow-list.md`
- 审批业务目录：`docs/api/endpoints/approval-flow-catalog.md`
- 详情：`docs/api/endpoints/approval-flow-detail.md`
- 保存草稿：`docs/api/endpoints/approval-flow-save-draft.md`
- 发布：`docs/api/endpoints/approval-flow-publish.md`
- 创建下一版本：`docs/api/endpoints/approval-flow-create-next-version.md`
- 停用：`docs/api/endpoints/approval-flow-disable.md`
- 删除：`docs/api/endpoints/approval-flow-delete.md`
