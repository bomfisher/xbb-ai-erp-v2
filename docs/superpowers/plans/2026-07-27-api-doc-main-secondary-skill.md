# API Main/Secondary Doc Skill Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在仓库中新增一个通用 command skill，根据用户输入的主题、范围和要求，生成或更新接口主文档与功能索引次文档。

**Architecture:** 新 skill 采用“主文档是唯一事实源、次文档是功能索引层”的双维模型，落在新的 `.claude/commands/gen-api-main-secondary/` 目录下。`SKILL.md` 负责顶层编排，辅助 markdown 文件分别承载输入协议、冲突决策、主文档模板、次文档模板与示例场景，避免所有规则堆进一个大文件。

**Tech Stack:** Claude command skill（Markdown）、仓库内现有 `docs/api` 接口文档、`docs/superpowers/specs` 设计文档、Bash 校验命令

## Global Constraints

- 对话永远在中文语境下。
- 项目架构DDD领域驱动设计。
- 主文档是唯一事实源；次文档只承担索引职责。
- 不内置任何具体业务映射知识。
- 不通过翻译、拼音或语义猜测自动生成业务标识。
- 仅依赖用户输入、当前仓库现有文档与当前仓库代码事实。
- 找到候选值不等于已确认事实；候选必须带来源。
- 结构化输入优先，自然语言输入解析不稳时必须反问用户。
- 目标主文档结构不匹配时，先提示用户确认是否重写。
- 不在次文档复制主文档完整正文。
- 不直接覆盖用户已有文档内容；优先增量更新，必要时暂停确认。
- 不创建 git commit，除非用户显式要求。

---

### Task 1: 创建 skill 骨架与输入协议

**Files:**
- Create: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Create: `.claude/commands/gen-api-main-secondary/input-contract.md`

**Interfaces:**
- Consumes: 用户原始输入字符串（结构化输入或自然语言输入）
- Produces: `SKILL.md` 的顶层入口章节；`input-contract.md` 中的统一输入模型：`subject`、`subjectLabel`、`scope`、`docMode`、`requirements`

- [ ] **Step 1: 先跑缺失校验，确认目标文件当前不存在**

Run: `test -f ".claude/commands/gen-api-main-secondary/SKILL.md" && test -f ".claude/commands/gen-api-main-secondary/input-contract.md"`
Expected: FAIL，返回非 0，因为目标文件尚未创建。

- [ ] **Step 2: 写 `input-contract.md`，固定输入协议与缺失字段处理规则**

```md
## 输入协议

### 结构化输入

支持以下字段：

- `subject`
- `subjectLabel`
- `scope`
- `docMode`
- `requirements`

### 自然语言输入

- 先解析出主题、范围、输出模式
- 无法稳定解析时停止并反问
- 不允许靠语义猜测补齐缺失关键字段

### 内部统一模型

- `subject`：主题标识
- `subjectLabel`：主题中文名
- `scope`：`list/create/edit/detail/...`
- `docMode`：`main/secondary/both`
- `requirements`：补充要求

### 缺失字段反问规则

以下字段缺失时必须反问：

- `subject` 与 `subjectLabel` 同时缺失
- `scope` 缺失
- `docMode` 缺失
```

- [ ] **Step 3: 写 `SKILL.md` 最小骨架，先把适用场景、目标、输入入口和暂停条件立起来**

```md
---
description: 根据用户输入生成或更新接口主文档与功能索引次文档
---

## 适用场景
- 用户希望为某个主题补接口主文档
- 用户希望按功能补列表、详情等次文档索引
- 用户希望按统一规则维护主文档与次文档边界

## 目标
- 主文档维护完整事实
- 次文档维护功能索引
- 不确定信息输出为待确认项

## 输入入口
- 先读取 `input-contract.md`
- 把用户输入归一化为统一模型

## 暂停并反问条件
- 缺少关键字段
- 候选主文档不唯一
- 代码与文档冲突且无法判断
```

- [ ] **Step 4: 运行结构校验，确认顶层 skill 和输入协议都具备必需章节**

Run: `rg -n '^## (适用场景|目标|输入入口|暂停并反问条件)$' .claude/commands/gen-api-main-secondary/SKILL.md && rg -n '^### (结构化输入|自然语言输入|内部统一模型|缺失字段反问规则)$' .claude/commands/gen-api-main-secondary/input-contract.md`
Expected: PASS，输出以上章节标题所在行号。

### Task 2: 补齐决策规则与主次文档模板

**Files:**
- Create: `.claude/commands/gen-api-main-secondary/decision-rules.md`
- Create: `.claude/commands/gen-api-main-secondary/main-template.md`
- Create: `.claude/commands/gen-api-main-secondary/secondary-template.md`

**Interfaces:**
- Consumes: Task 1 中的统一输入模型
- Produces: 决策矩阵；主文档模板章节；次文档模板字段结构

- [ ] **Step 1: 先跑缺失校验，确认辅助规则和模板文件当前不存在**

Run: `test -f ".claude/commands/gen-api-main-secondary/decision-rules.md" && test -f ".claude/commands/gen-api-main-secondary/main-template.md" && test -f ".claude/commands/gen-api-main-secondary/secondary-template.md"`
Expected: FAIL，返回非 0，因为三个文件尚未创建。

- [ ] **Step 2: 写 `decision-rules.md`，明确事实来源优先级、更新模式与冲突处理**

```md
## 事实来源优先级

1. 用户显式输入
2. 当前仓库现有主文档
3. 当前仓库现有次文档
4. `docs/superpowers/specs` 与 plans
5. 当前仓库代码事实

## 更新模式

- 主文档不存在：新建
- 主文档存在且结构匹配：增量更新
- 主文档存在但结构不匹配：暂停并询问是否重写
- 次文档默认增量更新，不主动整体重写

## 冲突处理

- 主文档与次文档冲突：以主文档为准
- 文档与代码冲突：列出候选与来源，停止拍板
- 候选主文档不唯一：暂停并反问

## 待确认输出格式

- `待确认：事项；候选：A / B；来源：...`
```

- [ ] **Step 3: 写 `main-template.md`，固定主文档的接口正文块结构**

```md
# <主题中文名>模块接口

## <功能章节>

### `<完整接口路径>`

- 请求方式：`POST`
- 入参：`<DTO>`
- 用途：<一句话用途>

#### 请求示例
```json
{}
```

#### 响应示例
```json
{}
```

#### 参数说明
- `字段名`：说明

#### 规则说明
- 规则 1
- 规则 2
```

- [ ] **Step 4: 写 `secondary-template.md`，固定次文档的索引记录结构**

```md
# 公共<功能>接口

## `<完整接口路径>`

- 所属业务：`<主题中文名>`
- 请求方式：`POST`
- 用途：<一句话用途>
- 差异点：<与同类接口不同之处>
- 主文档定位：`docs/api/<主文档文件名>.md`
```

- [ ] **Step 5: 运行模板校验，确认规则文件和模板文件包含关键章节**

Run: `rg -n '^## (事实来源优先级|更新模式|冲突处理|待确认输出格式)$' .claude/commands/gen-api-main-secondary/decision-rules.md && rg -n '^# <主题中文名>模块接口$|^## <功能章节>$|^### `<完整接口路径>`$' .claude/commands/gen-api-main-secondary/main-template.md && rg -n '^# 公共<功能>接口$|^## `<完整接口路径>`$|主文档定位' .claude/commands/gen-api-main-secondary/secondary-template.md`
Expected: PASS，三个文件都能匹配到约定结构。

### Task 3: 完成 `SKILL.md` 编排逻辑并接入辅助文档

**Files:**
- Modify: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Modify: `.claude/commands/gen-api-main-secondary/input-contract.md`
- Modify: `.claude/commands/gen-api-main-secondary/decision-rules.md`

**Interfaces:**
- Consumes: Task 1 与 Task 2 产出的输入模型、决策矩阵、模板结构
- Produces: `SKILL.md` 的完整执行流程：解析输入 → 扫描事实源 → 决定主文档/次文档 → 生成或更新 → 输出摘要与待确认项

- [ ] **Step 1: 先写失败校验，确认当前 `SKILL.md` 还没有完整执行流程章节**

Run: `rg -n '^## (执行流程|扫描范围|输出模板)$' .claude/commands/gen-api-main-secondary/SKILL.md`
Expected: FAIL，当前还找不到完整编排章节。

- [ ] **Step 2: 扩写 `SKILL.md`，补齐顶层执行流程与辅助文件使用顺序**

```md
## 执行流程
- 第一步：读取 `input-contract.md`，把用户输入归一化
- 第二步：扫描 `docs/api`、`docs/superpowers/specs` 与代码事实
- 第三步：根据 `decision-rules.md` 决定主文档、次文档与更新模式
- 第四步：先处理主文档，再处理次文档
- 第五步：按统一格式输出结果摘要与待确认项

## 扫描范围
- `docs/api/*.md`
- `docs/superpowers/specs/*.md`
- 相关 controller、DTO、VO、URL、业务枚举

## 输出模板
- 新建文档：...
- 更新文档：...
- 更新章节：...
- 事实来源：...
- 待确认：...
```

- [ ] **Step 3: 在 `input-contract.md` 补充 `docMode` 和 `scope` 的受支持值列表，避免 skill 解释漂移**

```md
### `scope` 支持值
- `list`
- `create`
- `edit`
- `detail`

### `docMode` 支持值
- `main`
- `secondary`
- `both`
```

- [ ] **Step 4: 在 `decision-rules.md` 补充“先主后次”和“不得复制完整正文”的硬约束**

```md
## 主次文档边界硬约束

- 主文档是唯一事实源
- 次文档不得复制主文档完整请求示例、响应示例和字段说明
- `docMode=both` 时必须先更新主文档，再更新次文档
```

- [ ] **Step 5: 运行联通校验，确认 `SKILL.md` 与两个辅助文件已经具备完整编排信息**

Run: `rg -n '^## (执行流程|扫描范围|输出模板)$' .claude/commands/gen-api-main-secondary/SKILL.md && rg -n '^### (`scope` 支持值|`docMode` 支持值)$' .claude/commands/gen-api-main-secondary/input-contract.md && rg -n '^## 主次文档边界硬约束$' .claude/commands/gen-api-main-secondary/decision-rules.md`
Expected: PASS，三个文件都能输出新增章节。

### Task 4: 增加示例场景并做端到端自检

**Files:**
- Create: `.claude/commands/gen-api-main-secondary/examples.md`
- Modify: `.claude/commands/gen-api-main-secondary/SKILL.md`
- Modify: `.claude/commands/gen-api-main-secondary/main-template.md`
- Modify: `.claude/commands/gen-api-main-secondary/secondary-template.md`

**Interfaces:**
- Consumes: 已完成的 skill 编排、模板与决策规则
- Produces: 面向使用者的示例场景；最终可交付的 command skill 目录结构

- [ ] **Step 1: 先跑缺失校验，确认示例文件当前不存在**

Run: `test -f ".claude/commands/gen-api-main-secondary/examples.md"`
Expected: FAIL，返回非 0，因为示例文件尚未创建。

- [ ] **Step 2: 写 `examples.md`，至少覆盖一个结构化输入场景和一个自然语言输入场景**

```md
## 结构化输入示例

- 输入：`subjectLabel=客户，scope=list,detail，docMode=both，requirements=优先增量更新`
- 期望行为：先定位客户主文档，再同步列表与详情相关次文档

## 自然语言输入示例

- 输入：`帮我补客户列表和详情的主文档与次文档，已有文档尽量不要重写`
- 期望行为：抽取主题、范围与更新偏好；缺字段时反问

## 冲突示例

- 输入：只给“采购”，但仓库中出现多个可能主文档
- 期望行为：输出候选与来源，暂停并询问用户
```

- [ ] **Step 3: 在 `SKILL.md` 末尾增加“使用顺序”与“示例入口”，把所有辅助文件串起来**

```md
## 使用顺序
1. 阅读 `input-contract.md`
2. 阅读 `decision-rules.md`
3. 按需套用 `main-template.md` 与 `secondary-template.md`
4. 参考 `examples.md` 输出结果
```

- [ ] **Step 4: 运行端到端目录校验与占位词扫描，确认新 skill 目录完整且没有遗留 TBD/TODO**

Run: `find .claude/commands/gen-api-main-secondary -maxdepth 1 -type f | sort && ! rg -n 'TBD|TODO|待补充|请填写' .claude/commands/gen-api-main-secondary`
Expected: PASS，目录中应至少包含 `SKILL.md`、`input-contract.md`、`decision-rules.md`、`main-template.md`、`secondary-template.md`、`examples.md`，并且没有占位词残留。

- [ ] **Step 5: 用当前仓库文档形态做一次人工走查，确认 skill 规则与现有文档风格兼容**

Run: `rg -n '^# |^## ' docs/api/customer-customer.md docs/api/common-list.md .claude/commands/gen-api-main-secondary/SKILL.md .claude/commands/gen-api-main-secondary/main-template.md .claude/commands/gen-api-main-secondary/secondary-template.md`
Expected: PASS，可以同时看到现有主文档、次文档与新模板的章节结构；若结构明显冲突，则先修正文案，再继续。
