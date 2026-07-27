# 文档目录职责与命名规则同步设计

## 1. 背景

当前仓库里已经引入了三类和文档维护相关的 command skill：

- `gen-api-main-secondary`
- `gen-api-md`
- `multi-player`

但它们对“主文档、次文档、API 接口文档”三类文档的目录职责还没有完全统一，已经暴露出以下问题：

- 主文档、次文档被错误写入 `docs/api`
- `docs/api` 同时承载知识文档与接口文档，职责混淆
- 新的主次文档规范没有被同步到 3 个 skill 和辅助文件
- 文件命名规则没有统一，后续 AI 难以稳定判断主文档与次文档

因此需要先统一一版“目录职责 + 命名规则 + skill 边界”，并将这一版规则同步到相关 skill 及必要辅助文件中。

## 2. 目标

本次设计目标是：

- 明确 `docs/kn` 只承载主文档、次文档和业务知识文档
- 明确 `docs/api` 只承载 API 接口文档
- 明确主文档文件名使用 `-m` 后缀
- 明确次文档文件名使用 `-s` 后缀
- 明确 `gen-api-main-secondary`、`gen-api-md`、`multi-player` 三个 skill 的目录职责边界
- 同步必要辅助文件，避免 skill 本体与模板口径不一致

## 3. 非目标

第一版明确不做以下事情：

- 不批量迁移现有 `docs/api` 中的历史文档
- 不统一重命名所有旧接口文档
- 不一次性修复所有旧目录引用
- 不修改与当前 3 个 skill 无关的其他 command skill
- 不新增 `docs/api` 的新命名后缀规则

本次只做“规则源同步”，不做全仓历史文档治理。

## 4. 目录职责

### 4.1 `docs/kn`

`docs/kn` 只用于存放知识型文档，包括：

- 主文档
- 次文档
- 业务知识文档
- 主文档与次文档之间的引用关系
- 业务知识文档与 API 接口文档的路径关系

### 4.2 `docs/api`

`docs/api` 只用于存放 API 接口文档。

这里的 API 接口文档，是指围绕接口路径、请求方式、入参、出参、规则说明编写的接口说明文档。

`docs/api` 不再承担主文档或次文档职责。

## 5. 命名规则

### 5.1 主文档

主文档统一使用 `-m` 后缀。

例如：

- `customer-m.md`
- `purchase-order-m.md`

### 5.2 次文档

次文档统一使用 `-s` 后缀。

例如：

- `list-s.md`
- `customer-list-s.md`

### 5.3 API 文档

`docs/api` 下的 API 接口文档命名规则保持现状。

本次不强推新的后缀命名，不增加 `-api` 等规则。

## 6. 三个 skill 的职责边界

### 6.1 `gen-api-main-secondary`

职责：

- 只负责生成或更新 `docs/kn` 下的主文档与次文档
- 主文档使用 `-m` 命名
- 次文档使用 `-s` 命名
- 不再把 `docs/api` 作为主文档或次文档的输出目录

需要同步的辅助文件包括：

- `input-contract.md`
- `decision-rules.md`
- `main-template.md`
- `secondary-template.md`
- `examples.md`

这些文件都要统一改成 `docs/kn` 目录口径与 `-m / -s` 命名口径。

### 6.2 `gen-api-md`

职责：

- 只负责生成或更新 `docs/api` 下的 API 接口文档
- 不负责生成主文档或次文档
- 当用户要求的是知识文档时，应交由 `gen-api-main-secondary`

需要同步的辅助文件包括：

- `template.md`

模板中必须明确这是 API 接口文档模板，而不是知识文档模板。

### 6.3 `multi-player`

职责：

- 作为编排器串联 `gen-api-main-secondary` 与 `gen-api-md`
- 先维护 `docs/kn` 下的主文档、次文档或知识文档
- 再维护 `docs/api` 下的 API 接口文档
- 最后在知识文档中维护 API 接口文档路径引用

`multi-player` 不自己定义新的目录规则，而是显式复用前两个 skill 的边界。

## 7. 统一行为规则

### 7.1 目录判定

- 目标是主文档、次文档、业务知识文档时，默认写入 `docs/kn`
- 目标是 API 接口文档时，默认写入 `docs/api`
- skill 不允许把知识文档和 API 接口文档混写到同一个目录

### 7.2 更新边界

- `gen-api-main-secondary` 只能改 `docs/kn`
- `gen-api-md` 只能改 `docs/api`
- `multi-player` 可以同时调用两者，但不能破坏目录边界

### 7.3 冲突处理

如果用户只说“补文档”，但没有说清是知识文档还是接口文档：

- skill 先根据上下文判断
- 判断不稳则反问用户

如果同一业务同时需要知识文档和接口文档：

- 业务结构、主次关系、引用路径写入 `docs/kn`
- 接口请求与响应说明写入 `docs/api`

## 8. 同步范围策略

本次采用最小同步策略：

- 只同步 3 个目标 skill
- 只同步这些 skill 所直接依赖的必要辅助文件
- 不做仓库级历史文档搬迁
- 不做历史命名统一

这是为了先把“规则源”收口，再逐步治理旧文档。

## 9. 推荐方案与结论

本次采用“方案 A：最小同步”。

具体含义是：

- 先统一 3 个 skill 与必要辅助文件的规则口径
- 先把 `docs/kn` 与 `docs/api` 的职责拆清
- 先把 `-m` 与 `-s` 的命名约束落到 skill 规则中
- 暂不大规模修改历史文档

这样可以用最小改动建立新的统一规则，同时避免本轮任务越界到历史文档批量治理。

## 10. 后续实现范围

第一版落地至少需要覆盖以下文件：

- `.claude/commands/gen-api-main-secondary/SKILL.md`
- `.claude/commands/gen-api-main-secondary/input-contract.md`
- `.claude/commands/gen-api-main-secondary/decision-rules.md`
- `.claude/commands/gen-api-main-secondary/main-template.md`
- `.claude/commands/gen-api-main-secondary/secondary-template.md`
- `.claude/commands/gen-api-main-secondary/examples.md`
- `.claude/commands/gen-api-md/SKILL.md`
- `.claude/commands/gen-api-md/template.md`
- `.claude/commands/multi-player/SKILL.md`

如果实现过程中发现某个辅助文件仍然保留旧口径，也应纳入同一轮同步，但不得扩大到无关 skill。