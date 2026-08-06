# Skill 文档架构同步改造 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 统一改造 `multi-player`、`gen-api-md`、`gen-api-main-secondary` 三个 skill，使其符合最新文档架构：API 原子接口文档、业务/功能聚合文档、双入口导航文档三层分工。

**Architecture:** 采用“三层文档架构 + 单一事实源”方案。`gen-api-md` 只维护 API 原子接口文档，原则上一个接口一份文档；`gen-api-main-secondary` 只维护业务聚合文档与功能聚合文档；`multi-player` 负责串联两者，并把新生成或更新的文档同步登记到双入口导航文档 `docs/kn/总目录.md` 中。所有聚合文档只做摘要、归类和跳转，不复制 API 原子文档中的完整请求/响应正文。

**Tech Stack:** Claude command skill（Markdown）、仓库内现有 `docs/api` / `docs/kn` 文档、`rg` / `find` / `git diff` 校验命令

## Global Constraints

- 对话与文档说明保持中文语境。
- 项目架构遵循 DDD 领域驱动设计。
- `gen-api-md` 只负责 API 文档，不负责业务/功能导航文档。
- `gen-api-main-secondary` 只负责业务聚合文档、功能聚合文档和双入口导航所需的知识层信息。
- `multi-player` 只做编排与同步，不自定义独立规则源。
- API 文档采用“一个接口一份文档”的原子事实源模型。
- 业务文档与功能文档采用“多个接口一份文档”的聚合模型。
- 双入口导航文档同时提供“按业务查”和“按功能查”两套入口。
- 聚合文档不得复制 API 原子文档的完整请求示例、响应示例、字段表。
- 不凭猜测补事实；不确定内容必须输出待确认项并带来源。
- 优先增量更新现有文件；只有结构明显冲突时才提示重写。
- 本轮只改 3 个 skill 及其直接依赖文件，不做全仓历史文档迁移。
- 不创建 git commit，除非用户显式要求。

---

### Task 1: 重写 `gen-api-md` 的职责边界为 API 原子接口文档

**Files:**
- Modify: `.claude/commands/gen-api-md/SKILL.md`
- Modify: `.claude/commands/gen-api-md/template.md`
- Modify: `.claude/commands/gen-api-md/cal-domain.md`

**Interfaces:**
- Consumes: 用户提供的接口代码范围、Controller/DTO/VO/URL 事实、已有 `docs/api` 文档
- Produces: `docs/api/endpoints/<domain>-<action>.md` 风格的 API 原子接口文档；每个文件只承载一个接口事实块

- [ ] **Step 1: 先跑基线检查，确认旧 skill 仍然把 `docs/api` 视为多接口聚合文档**

Run: `rg -n '如果接口文档内接口数量已经 >= 20 则新增一份接口文档|如果已有文档已有接口，根据当前代码，检查更新当前接口|在 `docs/api` 目录下创建 API 接口文档' .claude/commands/gen-api-md/SKILL.md`
Expected: PASS，能看到旧规则仍以“一个文件内维护多个接口”为前提。

- [ ] **Step 2: 改写 `SKILL.md`，把输出模型改成“一个接口一份文档”**

```md
---
description: 当代码新增接口或接口契约变化时，维护 docs/api/endpoints 下的 API 原子接口文档
---

## 适用场景
- agent 完成代码开发并通过验证后，新增了接口
- agent 完成代码开发并通过验证后，接口 URL、入参 DTO、出参 VO、请求方式发生变化
- 用户明确要求补或修某个接口的 API 文档

## 目标
- 只维护 `docs/api/endpoints` 下的 API 原子接口文档
- 一个接口一份文档
- 每份文档只维护一个接口的完整事实
- 如果用户要求维护业务/功能聚合文档，应转交 `gen-api-main-secondary`

## 执行流程
- 计算接口领域与动作标识
- 定位或创建目标 API 原子文档
- 依据当前代码事实更新该单一接口文档
- 输出本次新建/更新的 API 文档路径，供上层编排器继续同步
```

- [ ] **Step 3: 改写 `template.md`，去掉“一个文档多个接口”的暗示，固定为单接口模板**

```md
# <接口名称>

## 文档信息
- 领域：`<domain>`
- 控制器：`<Controller#method>`
- 请求方式：`POST /erp/v1/...`
- 聚合文档引用：`docs/kn/<business-doc>.md`（可后补）

## 请求示例
```json
{}
```

## 参数说明
| 字段 | 是否必填 | 备注 |
| --- | --- | --- |

## 响应示例
```json
{}
```

## 响应参数说明
| 字段 | 是否必返 | 备注 |
| --- | --- | --- |

## 规则说明
- 只描述当前这一个接口的规则
```

- [ ] **Step 4: 改写 `cal-domain.md`，补充文件命名与落盘规则**

```md
## 领域计算规则
1. 领域前缀：取接口所在 module 项目名的业务后缀，例如 `xbb-erp-module-customer` -> `customer`
2. 领域后缀：取 `/erp/v1/` 之后的接口第一段或动作语义，例如 `/erp/v1/customer/saveAndSubmit` -> `save-and-submit`
3. 文件名：`<领域前缀>-<领域后缀>.md`
4. 输出目录：`docs/api/endpoints/`
5. 一个接口只对应一个目标文件；禁止在一个文件内继续累积第二个接口正文
```

- [ ] **Step 5: 跑改造后校验，确认 `gen-api-md` 已完全切到原子接口文档模型**

Run: `rg -n 'docs/api/endpoints|一个接口一份文档|只维护一个接口的完整事实' .claude/commands/gen-api-md/SKILL.md .claude/commands/gen-api-md/template.md .claude/commands/gen-api-md/cal-domain.md && ! rg -n '>= 20|已有文档已有接口' .claude/commands/gen-api-md/SKILL.md`
Expected: PASS，能匹配到新规则，且旧的“20 个接口拆分”规则已消失。

### Task 2: 重写 `gen-api-main-secondary` 为业务/功能聚合文档维护器

**Files:**
- Modify: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Modify: `.claude/commands/gen-api-main-secondary/input-contract.md`
- Modify: `.claude/commands/gen-api-main-secondary/decision-rules.md`
- Modify: `.claude/commands/gen-api-main-secondary/main-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/secondary-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/examples.md`

**Interfaces:**
- Consumes: 用户输入的业务主题、功能范围、已有 `docs/kn` 文档、`docs/api/endpoints` API 原子文档、相关代码事实
- Produces: `docs/kn/<business>-m.md` 业务聚合文档；`docs/kn/<function>-s.md` 或 `<business>-<function>-s.md` 功能聚合文档；供 `multi-player` 同步导航的路径清单

- [ ] **Step 1: 先跑基线检查，确认旧模板仍把主文档写成完整请求/响应正文事实源**

Run: `rg -n '#### 请求示例|#### 响应示例|#### 参数说明' .claude/commands/gen-api-main-secondary/main-template.md && rg -n '主文档是唯一事实源' .claude/commands/gen-api-main-secondary/decision-rules.md`
Expected: PASS，说明当前模板仍然偏“完整正文主文档”模型，需要调整为聚合摘要 + 跳转模型。

- [ ] **Step 2: 改写 `SKILL.md`，把职责明确为“业务聚合 + 功能聚合”，API 仅作为事实源**

```md
## 目标
- 维护 `docs/kn` 下的业务聚合文档与功能聚合文档
- 业务聚合文档按业务组织多个接口摘要
- 功能聚合文档按功能组织多个接口摘要
- API 原子文档是接口完整事实源
- 聚合文档只维护摘要、差异点、跳转关系与章节组织

## 执行流程
- 读取输入协议，归一化 subject / subjectLabel / scope / docMode
- 扫描 `docs/kn`、`docs/api/endpoints`、相关 spec 与代码事实
- 判断需要更新的业务文档、功能文档和章节
- 先更新业务聚合文档，再更新功能聚合文档
- 输出本次更新的聚合文档路径与待确认项
```

- [ ] **Step 3: 改写 `input-contract.md` 与 `decision-rules.md`，补充新文档架构约束**

```md
### `docMode` 支持值
- `business`
- `function`
- `both`

### 输出目录约束
- 业务聚合文档输出到 `docs/kn/*-m.md`
- 功能聚合文档输出到 `docs/kn/*-s.md`
- `docs/api/endpoints` 只作为 API 事实源扫描
```

```md
## 文档边界硬约束
- API 原子文档是单接口完整事实源
- 业务聚合文档不得复制 API 原子文档的完整请求/响应/字段表
- 功能聚合文档不得复制 API 原子文档的完整请求/响应/字段表
- `docMode=both` 时必须先更新业务聚合文档，再更新功能聚合文档
```

- [ ] **Step 4: 改写 `main-template.md`、`secondary-template.md`、`examples.md`，把模板切成摘要跳转模型**

```md
# 客户-m

## 保存类接口

### `saveAndSubmit`
- 接口路径：`POST /erp/v1/customer/saveAndSubmit`
- 用途：客户正式保存并提交
- 差异点：执行严格校验并正式落库
- API 文档：`docs/api/endpoints/customer-save-and-submit.md`

### `saveDraft`
- 接口路径：`POST /erp/v1/customer/saveDraft`
- 用途：客户草稿保存
- 差异点：执行宽松校验，仅写入草稿存储
- API 文档：`docs/api/endpoints/customer-save-draft.md`
```

```md
# save-s

## 客户
- 接口：`POST /erp/v1/customer/saveAndSubmit`
- 用途：客户正式保存
- 差异点：支持主档 + 子档正式落库
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-save-and-submit.md`
```

```md
## 结构化输入示例
- 输入：`subjectLabel=客户，scope=save,draft，docMode=both`
- 期望行为：更新 `docs/kn/customer-m.md` 的保存章节，并同步 `docs/kn/save-s.md` 中的客户保存索引
```

- [ ] **Step 5: 跑改造后校验，确认 `gen-api-main-secondary` 已不再把自己当作接口完整正文维护器**

Run: `rg -n 'API 文档：`docs/api/endpoints/|业务聚合文档|功能聚合文档|docMode=both 时必须先更新业务聚合文档' .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-main-secondary/input-contract.md .claude/commands/gen-api-main-secondary/decision-rules.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md .claude/commands/gen-api-main-secondary/examples.md && ! rg -n '#### 请求示例|#### 响应示例|#### 参数说明' .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md`
Expected: PASS，能匹配到跳转模型和新边界，且模板中不再含完整接口正文块。

### Task 3: 改写 `multi-player`，串联两个子 skill 并同步双入口导航

**Files:**
- Modify: `.claude/commands/multi-player/SKILL.md`
- Modify: `docs/kn/总目录.md`

**Interfaces:**
- Consumes: `gen-api-main-secondary` 输出的业务/功能聚合文档路径；`gen-api-md` 输出的 API 原子文档路径
- Produces: 统一编排顺序；`docs/kn/总目录.md` 中“按业务查 / 按功能查”导航条目，以及每个交叉点对 API 文档的跳转引用

- [ ] **Step 1: 先跑基线检查，确认旧 `multi-player` 还只写了粗粒度的三步描述**

Run: `rg -n '先执行|再执行|最后' .claude/commands/multi-player/SKILL.md`
Expected: PASS，只能看到旧的粗粒度编排说明，没有新导航同步规则。

- [ ] **Step 2: 改写 `multi-player/SKILL.md`，把编排顺序和同步对象写清楚**

```md
## 执行流程
- 第一步：执行 `gen-api-md`，生成或更新 `docs/api/endpoints` 下的 API 原子接口文档
- 第二步：执行 `gen-api-main-secondary`，生成或更新 `docs/kn` 下的业务聚合文档与功能聚合文档
- 第三步：根据两个子 skill 的结果，更新 `docs/kn/总目录.md`
- 第四步：在双入口导航中同步维护：业务入口、功能入口、交叉点下的 API 文档跳转

## 导航同步规则
- “按业务查”下列出业务聚合文档，并在章节内挂接对应功能与 API 文档
- “按功能查”下列出功能聚合文档，并在条目内挂接对应业务与 API 文档
- 同一接口在导航中可出现多次索引，但完整事实只保留在 `docs/api/endpoints`
```

- [ ] **Step 3: 重写 `docs/kn/总目录.md` 为双入口导航骨架**

```md
# 总目录

## 文档规则
- API 原子文档：`docs/api/endpoints/*.md`
- 业务聚合文档：`docs/kn/*-m.md`
- 功能聚合文档：`docs/kn/*-s.md`
- 完整接口事实只维护在 API 原子文档

## 按业务查

### 客户
- 业务文档：`docs/kn/customer-m.md`
- 保存：`docs/kn/save-s.md`
- 草稿：`docs/kn/draft-s.md`
- API：`docs/api/endpoints/customer-save-draft.md`
- API：`docs/api/endpoints/customer-save-and-submit.md`

## 按功能查

### 保存
- 功能文档：`docs/kn/save-s.md`
- 客户：`docs/kn/customer-m.md`
- API：`docs/api/endpoints/customer-save-and-submit.md`

### 草稿
- 功能文档：`docs/kn/draft-s.md`
- 客户：`docs/kn/customer-m.md`
- API：`docs/api/endpoints/customer-save-draft.md`
```

- [ ] **Step 4: 在 `multi-player/SKILL.md` 中补充冲突与增量更新规则，避免总目录被整页重写**

```md
## 边界限制
- 只维护当前主题相关的业务入口、功能入口和 API 跳转条目
- 优先增量更新 `docs/kn/总目录.md` 中的对应小节
- 如果现有总目录结构无法稳定定位小节，先输出待确认，再决定是否重写目录骨架
- 不在总目录复制 API 文档中的请求/响应正文
```

- [ ] **Step 5: 跑改造后校验，确认编排与双入口导航规则已经落齐**

Run: `rg -n 'docs/api/endpoints|按业务查|按功能查|导航同步规则|增量更新 `docs/kn/总目录.md`' .claude/commands/multi-player/SKILL.md docs/kn/总目录.md`
Expected: PASS，`multi-player` 与 `docs/kn/总目录.md` 都能匹配到新导航结构与同步规则。

### Task 4: 做跨 skill 一致性校验，保证三层文档架构口径完全一致

**Files:**
- Modify: `.claude/commands/gen-api-md/SKILL.md`
- Modify: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Modify: `.claude/commands/multi-player/SKILL.md`
- Modify: `.claude/commands/gen-api-md/template.md`
- Modify: `.claude/commands/gen-api-main-secondary/main-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/secondary-template.md`
- Modify: `docs/kn/总目录.md`

**Interfaces:**
- Consumes: Task 1-3 已完成的 skill 文案、模板、导航骨架
- Produces: 三个 skill 之间一致的目录口径、输出口径、同步顺序和跳转关系

- [ ] **Step 1: 运行目录口径一致性检查，确认所有文件都使用同一套目录边界描述**

Run: `rg -n 'docs/api/endpoints|docs/kn|业务聚合文档|功能聚合文档|API 原子文档' .claude/commands/gen-api-md/SKILL.md .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/multi-player/SKILL.md .claude/commands/gen-api-md/template.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md docs/kn/总目录.md`
Expected: PASS，所有文件都能命中新架构关键词，没有互相矛盾的目录描述。

- [ ] **Step 2: 运行旧口径残留扫描，清掉“主文档完整事实源”和“一个文件多个 API 接口”等旧说法**

Run: `! rg -n '主文档维护完整事实|如果接口文档内接口数量已经 >= 20|已有文档已有接口|docs/api 下的 API 接口文档，不用于主文档、次文档或业务知识文档。.*示例' .claude/commands/gen-api-md .claude/commands/gen-api-main-secondary .claude/commands/multi-player`
Expected: PASS，不再匹配旧架构残留表述。

- [ ] **Step 3: 运行占位词和冲突词扫描，保证文档已可直接执行**

Run: `! rg -n 'TBD|TODO|待补充|请填写|示例值|这里填写|后续补充' .claude/commands/gen-api-md .claude/commands/gen-api-main-secondary .claude/commands/multi-player docs/kn/总目录.md`
Expected: PASS，没有遗留占位词。

- [ ] **Step 4: 运行最终 diff 走查命令，人工确认改动只落在计划范围内**

Run: `git diff -- .claude/commands/gen-api-md .claude/commands/gen-api-main-secondary .claude/commands/multi-player docs/kn/总目录.md`
Expected: PASS，只出现本计划列出的 3 个 skill、依赖模板和双入口导航文档改动；若 diff 涉及其他路径，先回退越界修改再继续。

- [ ] **Step 5: 记录执行完成后的验证结论，准备进入实现执行模式**

```md
验证结论：
- `gen-api-md` 已切换为 API 原子接口文档维护器
- `gen-api-main-secondary` 已切换为业务/功能聚合文档维护器
- `multi-player` 已能把两个子 skill 的产出同步进双入口导航
- `docs/kn/总目录.md` 已形成“按业务查 / 按功能查”的双入口导航骨架
```
