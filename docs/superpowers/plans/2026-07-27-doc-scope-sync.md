# Doc Scope Sync Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 同步 3 个文档维护 skill 与必要辅助文件的目录职责和命名规则，使主文档、次文档统一落到 `docs/kn`，API 接口文档统一收口到 `docs/api`。

**Architecture:** 本次采用最小同步策略，只修改 `.claude/commands/gen-api-main-secondary/`、`.claude/commands/gen-api-md/`、`.claude/commands/multi-player/` 及它们直接依赖的辅助文件。`gen-api-main-secondary` 负责 `docs/kn` 下的 `-m / -s` 文档规则，`gen-api-md` 负责 `docs/api` 下的 API 接口文档规则，`multi-player` 负责按边界编排两者。

**Tech Stack:** Claude command skill（Markdown）、仓库内 skill 辅助模板、Bash/rg 校验命令

## Global Constraints

- 对话永远在中文语境下。
- 项目架构DDD领域驱动设计。
- `docs/kn` 只承载主文档、次文档和业务知识文档。
- `docs/api` 只承载 API 接口文档。
- 主文档统一使用 `-m` 后缀。
- 次文档统一使用 `-s` 后缀。
- API 接口文档命名规则保持现状，不新增后缀规则。
- 采用方案 A：只做最小同步，不批量迁移历史文档。
- 只同步 3 个目标 skill 与必要辅助文件，不扩大到无关 skill。
- 不创建 git commit，除非用户显式要求。

---

### Task 1: 同步 `gen-api-main-secondary` 的目录与命名规则

**Files:**
- Modify: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Modify: `.claude/commands/gen-api-main-secondary/input-contract.md`
- Modify: `.claude/commands/gen-api-main-secondary/decision-rules.md`
- Modify: `.claude/commands/gen-api-main-secondary/main-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/secondary-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/examples.md`

**Interfaces:**
- Consumes: `docs/superpowers/specs/2026-07-27-doc-scope-sync-design.md`
- Produces: `gen-api-main-secondary` 全套规则统一改为 `docs/kn` + `-m / -s`

- [ ] **Step 1: 先写失败校验，确认当前文件中仍然残留 `docs/api` 作为主/次文档目标目录**

Run: `rg -n 'docs/api|主文档定位：`docs/api/' .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-main-secondary/input-contract.md .claude/commands/gen-api-main-secondary/decision-rules.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md .claude/commands/gen-api-main-secondary/examples.md`
Expected: PASS，当前应能匹配到旧口径，证明还未同步。

- [ ] **Step 2: 更新 `SKILL.md`，把主文档/次文档目标目录统一改成 `docs/kn`**

```md
## 目标
- 主文档维护完整事实，输出到 `docs/kn`
- 次文档维护功能索引，输出到 `docs/kn`
- 主文档使用 `-m` 后缀，次文档使用 `-s` 后缀

## 执行流程
- 第二步：扫描 `docs/kn`、`docs/api`、`docs/superpowers/specs` 与代码事实
- 第三步：根据 `decision-rules.md` 决定 `docs/kn` 下的主文档、次文档与更新模式
```

- [ ] **Step 3: 更新 `input-contract.md` 与 `decision-rules.md`，把输入和规则都改成 `docs/kn` 口径**

```md
### 输出目录约束
- 主文档输出到 `docs/kn`
- 次文档输出到 `docs/kn`
- API 接口文档不在本 skill 中维护
```

```md
## 目录与命名硬约束
- 主文档输出目录：`docs/kn`
- 次文档输出目录：`docs/kn`
- 主文档文件名使用 `-m`
- 次文档文件名使用 `-s`
```

- [ ] **Step 4: 更新 `main-template.md`、`secondary-template.md`、`examples.md`，把示例路径和命名改成 `docs/kn` + `-m / -s`**

```md
# customer-m.md

## 列表

### `/erp/v1/customer/list`
```

```md
# customer-list-s.md

## `/erp/v1/customer/list`

- 主文档定位：`docs/kn/customer-m.md`
```

```md
- 输入：`subjectLabel=客户，scope=list,detail，docMode=both`
- 期望行为：生成 `docs/kn/customer-m.md`，并同步 `docs/kn/customer-list-s.md`
```

- [ ] **Step 5: 运行联通校验，确认 `gen-api-main-secondary` 已不再把 `docs/api` 当作主/次文档目录**

Run: `! rg -n 'docs/api|主文档定位：`docs/api/' .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-main-secondary/input-contract.md .claude/commands/gen-api-main-secondary/decision-rules.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md .claude/commands/gen-api-main-secondary/examples.md && rg -n 'docs/kn|\-m\.md|\-s\.md' .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-main-secondary/input-contract.md .claude/commands/gen-api-main-secondary/decision-rules.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md .claude/commands/gen-api-main-secondary/examples.md`
Expected: PASS，旧目录口径消失，新目录与命名规则全部可见。

### Task 2: 同步 `gen-api-md` 的 API 文档职责边界

**Files:**
- Modify: `.claude/commands/gen-api-md/SKILL.md`
- Modify: `.claude/commands/gen-api-md/template.md`

**Interfaces:**
- Consumes: `docs/superpowers/specs/2026-07-27-doc-scope-sync-design.md`
- Produces: `gen-api-md` 明确只维护 `docs/api` 下的 API 接口文档

- [ ] **Step 1: 先写失败校验，确认当前 `gen-api-md` 还没有明确排除主文档和次文档职责**

Run: `rg -n '主文档|次文档|知识文档' .claude/commands/gen-api-md/SKILL.md .claude/commands/gen-api-md/template.md`
Expected: FAIL 或输出不足，说明当前边界尚未写清。

- [ ] **Step 2: 更新 `SKILL.md`，把职责收紧为只处理 `docs/api` API 接口文档**

```md
## 适用场景
当接口新增或接口出入参、URL 发生变化时，在 `docs/api` 下创建或更新 API 接口文档。

## 执行流程
- 只维护 `docs/api` 下的接口文档
- 如果用户要求的是主文档、次文档或业务知识文档，应转交 `gen-api-main-secondary`
```

- [ ] **Step 3: 更新 `template.md`，明确这是 API 接口文档模板，并补一句目录职责说明**

```md
# API 接口文档模板

> 本模板仅用于 `docs/api` 下的 API 接口文档，不用于主文档、次文档或业务知识文档。
```

- [ ] **Step 4: 运行边界校验，确认 `gen-api-md` 现在明确只负责 `docs/api`**

Run: `rg -n 'docs/api|API 接口文档|不用于主文档、次文档' .claude/commands/gen-api-md/SKILL.md .claude/commands/gen-api-md/template.md`
Expected: PASS，两个文件都明确写出 `docs/api` 与 API 职责。

### Task 3: 同步 `multi-player` 的编排边界

**Files:**
- Modify: `.claude/commands/multi-player/SKILL.md`

**Interfaces:**
- Consumes: `gen-api-main-secondary` 与 `gen-api-md` 的新边界
- Produces: `multi-player` 串联 `docs/kn` 与 `docs/api` 的分工流程

- [ ] **Step 1: 先写失败校验，确认当前 `multi-player` 还没有显式写出 `docs/kn` / `docs/api` 分工**

Run: `rg -n 'docs/kn|docs/api' .claude/commands/multi-player/SKILL.md`
Expected: FAIL 或输出缺失，说明目录分工尚未同步。

- [ ] **Step 2: 更新 `multi-player/SKILL.md`，把编排流程改成“先 `docs/kn`，后 `docs/api`，最后回写引用”**

```md
## 执行流程
- 先执行 `.claude/commands/gen-api-main-secondary/SKILL.md`，维护 `docs/kn` 下的主文档、次文档或业务知识文档
- 再执行 `.claude/commands/gen-api-md/SKILL.md`，维护 `docs/api` 下的 API 接口文档
- 最后在 `docs/kn` 的知识文档中更新维护 `docs/api` 接口文档路径
```

- [ ] **Step 3: 运行流程校验，确认 `multi-player` 已经显式引用两个目录职责**

Run: `rg -n 'docs/kn|docs/api|gen-api-main-secondary|gen-api-md' .claude/commands/multi-player/SKILL.md`
Expected: PASS，流程中同时出现两个目录与两个 skill。

### Task 4: 收口验证与文档对齐

**Files:**
- Test: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Test: `.claude/commands/gen-api-main-secondary/input-contract.md`
- Test: `.claude/commands/gen-api-main-secondary/decision-rules.md`
- Test: `.claude/commands/gen-api-main-secondary/main-template.md`
- Test: `.claude/commands/gen-api-main-secondary/secondary-template.md`
- Test: `.claude/commands/gen-api-main-secondary/examples.md`
- Test: `.claude/commands/gen-api-md/SKILL.md`
- Test: `.claude/commands/gen-api-md/template.md`
- Test: `.claude/commands/multi-player/SKILL.md`

**Interfaces:**
- Consumes: 前 3 个任务的全部输出
- Produces: 3 个 skill 与必要辅助文件口径一致的最终状态

- [ ] **Step 1: 运行全量占位词与旧口径扫描**

Run: `! rg -n 'TBD|TODO|待补充|请填写|docs/api.*主文档|docs/api.*次文档|主文档定位：`docs/api/' .claude/commands/gen-api-main-secondary .claude/commands/gen-api-md .claude/commands/multi-player`
Expected: PASS，不再出现旧目录职责或占位词。

- [ ] **Step 2: 运行全量新口径扫描**

Run: `rg -n 'docs/kn|docs/api|\-m\.md|\-s\.md' .claude/commands/gen-api-main-secondary .claude/commands/gen-api-md .claude/commands/multi-player`
Expected: PASS，所有需要的新口径都能被检索到。

- [ ] **Step 3: 人工对照 spec 走查关键条款**

Run: `rg -n 'docs/kn|docs/api|\-m|\-s|最小同步' docs/superpowers/specs/2026-07-27-doc-scope-sync-design.md .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-md/SKILL.md .claude/commands/multi-player/SKILL.md`
Expected: PASS，可以在 spec 与 3 个 skill 本体中同时看到相同关键口径。
