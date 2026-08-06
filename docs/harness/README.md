# ERP Harness

本目录是研发 Harness 的唯一入口：把需求输入、规则、实现、验证、文档和交付串成可重复执行的流程。`AGENTS.md`、`CLAUDE.md` 与各 SKILL 只负责要求 Agent 读取和执行本目录规则，不重复维护业务规范。

## 交付流程

```text
功能规格 -> 变更范围/工作树 -> DDD 设计与代码生成 -> 实现
    -> Harness 校验 -> 模块测试 -> API/聚合文档 -> CI -> 交付报告
```

1. 用 `功能交付输入模板.md` 明确业务目标、边界、验收标准、数据变化与接口影响。
2. 评估是否创建工作树；多人或相互独立的功能默认使用 `scripts/feature-worktree.sh create`。
3. 新模块先编写 YAML 规格并执行代码生成器 `dry-run`；已有模块按领域边界实现。
4. 执行 `scripts/harness-verify.sh`；再运行受影响 Maven 模块的测试。
5. 接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航。
6. 在交付报告中列出需求范围、变更文件、验证命令/结果、文档变化及待确认项。

## 文档索引

- `工程规则唯一事实源.md`：分层、命名、异常、安全与 Agent 规则。
- `功能交付输入模板.md`：功能开发前的最小输入和验收契约。
- `数据库迁移规范.md`：Flyway 的启用、基线和 SQL 迁移规则。
- `Flyway基线记录.md`：当前 `V1` 基线的来源、范围与使用边界。
- `前后端契约规范.md`：接口原子文档、前端联调和变更控制。
- `CI门禁规范.md`：GitHub Actions 工作流、分支保护与门禁分级。
- `验证矩阵.md`：按变更类型选择校验和证据。
- `发布检查清单.md`：合并与部署前的人工确认项。
- `../guide/采购模块保存入库初始化规范.md`：采购模块保存接口对数据库非空字段的初始化和临时占位规则。

## 本地命令

```bash
scripts/harness-verify.sh
scripts/harness-verify.sh --module xbb-erp-module-customer
scripts/harness-ci.sh xbb-erp-module-customer
scripts/feature-worktree.sh list
```

GitHub Actions 工作流位于 `.github/workflows/harness-verify.yml`；Required status check 需要仓库管理员手动配置。
