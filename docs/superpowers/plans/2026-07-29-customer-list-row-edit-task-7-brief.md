# Task 7 Brief

## 任务定位
收尾客户列表行内编辑方案：同步 `docs/api` 接口文档，并跑后端/前端目标测试，确认从公共 `rowAction` 到客户 `updateItem` 的整条链路通过。

## Global Constraints
- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 用户已明确：不创建worktree，不允许提交，使用子代理模式。
- 本任务只维护 `docs/api` 下的接口文档，不做主文档、次文档或业务知识文档。
- 行内动作来源走后端元数据；本次只落地客户 `EDIT`，不提前接入其他动作。
- 完成开发任务并通过验证后，执行 `gen-api-md` 规则；本轮 domain 已可直接落到 `common-list` 与 `customer-customer`，无需再扩展其他文档。
- 不要创建 git commit。

## Files
- Modify: `/Users/bomfish/xbb-ai-erp-v2/docs/api/common-list.md`
- Modify: `/Users/bomfish/xbb-ai-erp-v2/docs/api/customer-customer.md`
- Modify: `/Users/bomfish/xbb-ai-erp-v2/docs/superpowers/plans/2026-07-29-customer-list-row-edit-task-7-report.md`

## Existing Context
- `/erp/v1/common/list/rowAction` 已在后端实现，并由客户列表首版返回单个 `EDIT` 动作。
- 实际代码里 `confirmType` 为 `"NONE"`，不是 `null`。
- `CustomerAdminController` 已存在 `POST /erp/v1/customer/updateItem`，Task 5/6 已在前端接通编辑抽屉初始化链路。
- `docs/api/common-list.md` 当前已有 `rowAction` 章节，但响应示例和字段说明仍写成 `confirmType: null` / “未配置时返回 null”，与当前代码不一致。
- `docs/api/customer-customer.md` 当前已有 `updateItem` 章节，但还可以补清楚它作为编辑抽屉初始化接口的用途。

## Interfaces
- Consumes:
  - `POST /erp/v1/common/list/rowAction`
  - `POST /erp/v1/customer/updateItem`
- Produces:
  - refreshed `docs/api/common-list.md` for `rowAction`
  - refreshed `docs/api/customer-customer.md` for customer edit initialization
  - final verification report

## Required Doc Changes
### `docs/api/common-list.md`
把 `rowAction` 章节刷新为与当前代码一致，至少包含：
- 请求 URL：`POST /erp/v1/common/list/rowAction`
- 入参：`ListCommonQueryDTO`
- 出参：`ResultVO<ListRowActionVO>`
- 示例中的动作项：
```json
{
  "actionCode": "EDIT",
  "actionName": "编辑",
  "sort": 10,
  "showMode": "PRIMARY",
  "confirmType": "NONE"
}
```
- 参数说明里把 `confirmType` 改成当前客户列表首版返回 `NONE`，不要再写 `null`。

### `docs/api/customer-customer.md`
至少补清楚以下事实：
- `POST /erp/v1/customer/updateItem` 是编辑抽屉初始化接口。
- 入参 `id` 来自客户列表行数据主键。
- 返回结构与 `addItem` 一致，前端据此加载编辑表单。
- 当前客户列表页的编辑入口由公共 `/erp/v1/common/list/rowAction` 下发 `EDIT` 动作后触发。

## Required Commands
后端验证：
- `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
- `mvn -pl xbb-erp-module-customer -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`

前端验证：
- `pnpm --dir /Users/bomfish/xbb-ai-erp-v2-front --filter @xbb-erp/admin-web exec vitest run src/components/list/ListRowActions.spec.ts src/components/list/ListDataTable.spec.ts src/modules/customer/list/CustomerListPage.spec.ts`

Expected:
- 后端公共列表元数据测试通过
- 客户 provider 测试通过
- 前端 3 组目标 spec 全部通过

## Additional Execution Rules
- 文档只改 `docs/api/common-list.md` 与 `docs/api/customer-customer.md`，不要新建其他 API 文档。
- 如果你判断 `customer-customer.md` 当前某部分已经准确，不要大改格式，只做最小补充。
- 需要把执行结果完整写入：`/Users/bomfish/xbb-ai-erp-v2/docs/superpowers/plans/2026-07-29-customer-list-row-edit-task-7-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
- 若 `gen-api-md` 规则与现有文档已一致，可在报告里说明本次直接按规则刷新现有文档，无需新增文档。