---
name: business-select-query-endpoints
description: 在本后端仓库为可被其他模块选择的业务对象生成标准业务选择查询接口。用于新增或扩展 businessCode 的 quickSearch、dialogSearch、getById 能力，或为前端公共业务选择注册表提供标准端点时。
---

# 业务选择后端接口

先阅读 `AGENTS.md`、`docs/harness/README.md`、`docs/harness/工程规则唯一事实源.md`、目标模块代码及相关 API 原子文档；修改前检查工作区并确认 worktree。

## 固定契约

- 字段元数据只返回 `businessSelectConfig: { businessCode: "..." }`；禁止返回 URL、请求体、占位文案、标题、`businessType` 或单多选语义。
- 控制器路径固定为 `POST /erp/v1/{moduleApiName}/{businessName}/businessSelect/quickSearch`、`dialogSearch`、`getById`。
- 入口 DTO 继承 `BaseDTO`，包含 `keyword`、`pageNum`、`pageSize`、`id`；所有查询必须校验 `corpid` 并按租户隔离。
- 候选 VO 固定提供 `id`、可选 `code`、`name`、`label`；弹窗返回 `ListBaseVO`。
- Controller 只做 HTTP 转发和 `ResultVO.success()` 包装；查询经 Application Service 与 Domain Repository，禁止 Controller 直连 Mapper。

## 实施

1. 确认业务编码和目标模块；复用模块现有 Repository，禁止由消费方模块或前端页面新增选择接口。
2. 生成 `*BusinessSelectQueryDTO`、`*BusinessSelectOptionVO`，在 Query Application Service 实现关键字查询、分页弹窗和按 ID 回显。
3. 关键字至少匹配稳定编码与名称；`getById` 对跨租户或不存在数据返回 `null`，不泄漏数据。
4. 经模块 Admin Application Service 转发，并在既有 Admin Controller 注册三条标准端点。
5. 为租户隔离、关键字、分页、按 ID 回显和空结果补测试；新增接口时维护 `docs/api/endpoints/` 原子文档及导航。
6. 运行 `scripts/harness-verify.sh --module <module>` 和受影响 Maven 测试；前端联调前确认公共注册表使用相同 `businessCode` 和路径。
