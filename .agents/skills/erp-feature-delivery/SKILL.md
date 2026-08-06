---
name: erp-feature-delivery
description: 在本仓库交付一个完整 ERP 功能时编排需求规格、DDD 模块设计、代码生成、列表与表单实现、保存链路、测试、Flyway 迁移和接口文档。用于新增或跨越多个层次修改业务功能、业务模块或接口契约的任务。
---

# ERP 功能交付

以仓库 Harness 作为功能开发的唯一执行路径。先读 `docs/harness/README.md`，再按需求选择引用的专业 SKILL。

## 输入确认

1. 阅读 `docs/harness/功能交付输入模板.md`，补齐业务目标、聚合边界、页面/接口、数据变化和验收标准。
2. 对工作树、历史库基线、生产发布、权限策略、外部接口和前端兼容等不确定事项，先向用户确认。
3. 检查 `git status --short`，保护已有改动；仅在用户同意后创建工作树。

## 实施路由

- 新业务模块：使用 `.claude/commands/init-module/SKILL.md`，先准备 YAML 规格并执行 `dry-run`；随后使用 `$ddd-business-module-development` 完成业务用例。
- 列表、新建、编辑、保存或草稿：使用 `$ddd-business-module-development`，遵守公共列表、字段场景和主子档保存协议。
- 接口新增或契约变化：完成验证后执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航。
- 数据库结构变化：先获得历史库基线确认，再遵守 `docs/harness/数据库迁移规范.md` 新增 Flyway 迁移。

## 交付门禁

1. 执行 `scripts/harness-verify.sh`；新模块额外执行 `scripts/harness-verify.sh --module xbb-erp-module-<name>`。
2. 运行受影响 Maven 模块及依赖模块测试；记录命令、结果和无法验证的原因。
3. 接口契约变化时完成文档编排；交付报告列出变更范围、验证证据、迁移编号和待确认项。
4. 不提交真实凭据、私钥或 Token；不绕过失败的 Harness 或 CI 检查。
