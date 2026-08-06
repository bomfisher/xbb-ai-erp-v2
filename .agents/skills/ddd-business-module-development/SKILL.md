---
name: ddd-business-module-development
description: 在本仓库按 DDD 约束新增或扩展 ERP 业务模块、列表页、新建页、编辑页、保存提交或草稿能力。用于涉及 `xbb-erp-module-*`、`ListCommonController`、表单字段元数据、主子档保存、Repository 分层或相关接口文档的开发任务。
---

# DDD 业务模块开发

以 `xbb-erp-module-customer` 为架构参考实现业务能力，保持 HTTP、应用、领域和基础设施分层，并同步页面协议与文档。详细契约在仓库根目录的 `docs/guide/DDD业务模块开发指南.md`。

## 开始前

1. 阅读 `AGENTS.md`、`docs/guide/DDD业务模块开发指南.md`、目标模块的 Controller/AppService/Repository，以及相关 `docs/api/endpoints/` 文档。
2. 通过 `git status --short` 识别已有用户改动；只修改当前需求范围内的文件，不还原或覆盖无关变更。
3. 确认需求类型：新模块、列表、表单初始化、保存提交、草稿，或其组合；先确定领域对象、聚合边界和接口契约。

## 实施流程

### 1. 建立 DDD 边界

- 在 `domain.model` 定义领域模型，在 `domain.repository` 定义领域接口；需要查询组合时使用 `domain.pojo`。
- 在 `infrastructure.persistence` 放置 `*PO`、Mapper、Convertor 和 Repository 实现；禁止 Controller 直连 Mapper。
- 在 `application` 编排用例、转换对象、执行业务校验和事务；保持 Domain 不依赖 HTTP 与持久化技术。
- 在 `admin.dto`、`admin.vo` 和 Controller 固定 HTTP 契约；Controller 只转发调用并用 `ResultVO.success()` 返回。

### 2. 实现列表页

- 同时实现 `POST /erp/v1/{business}/list` 的数据查询和 `ListMetaProvider` 的字段、筛选、按钮、行操作元数据。
- 用 `businessCode()` 接入 `ListMetaRegistry`；用 `*ListQueryAdapter` 将动态筛选条件映射到列名和操作符白名单。
- 从 `LIST` 场景字段工厂生成表头；列表子档摘要必须批量查询，不能在行循环中访问数据库。

### 3. 实现新建与编辑页

- 新建使用 `BaseDTO` 和 `CREATE` 场景，返回 `SaveItemVO` 的 `headList + data`，初始化空子档与 `sectionState=0`。
- 编辑使用 `IdBaseDTO` 和 `UPDATE` 场景，回填主档和子档，并按已有子档数据设置 `sectionState`。
- 将字段、枚举选项、必填和可编辑规则集中在 `*FieldFactory`、`*FieldRule` 和字段枚举，前端不硬编码字段结构。

### 4. 实现保存与草稿

- 保存使用专用 `*SubmitSaveDTO` 和应用上下文 Pojo，按“协议校验 → 通用字段校验 → 业务校验 → 主子档同步”的顺序执行。
- 以 `sectionState` 过滤关闭的子档；显式处理默认项、引用约束和删除语义；保存主子档时设置清晰的事务边界。
- 草稿与正式保存分离：草稿使用应用 Port 持久化并执行宽松校验；正式保存成功后，才按请求中原 `draftCode` 清除草稿。

## 强制约束

- 所有非脚本接口请求参数使用 DTO 并继承 `BaseDTO`；返回值使用 VO 或 `BaseVO`，成功响应使用 `ResultVO.success()`。
- 数据库对象使用 `*PO` 后缀；数据库布尔状态使用 `Integer`；枚举使用 `*Enum`；内部中转对象使用 `*Pojo`。
- 所有主动业务失败抛 `BizException`。避免循环查询数据库，避免由前端传入数据库列名、SQL 片段或审计字段。
- 不因参考模块现有的实现细节而跳过分页计数、事务、空值或权限等需求；以本次业务规则为准补齐约束。

## 验证与文档

1. 检查层次依赖、对象后缀、`BizException`、批量查询、保存事务和 `sectionState` 语义。
2. 运行目标模块最相关的测试或 Maven 验证；无法运行时说明具体原因，不要声称已验证。
3. 新增或变更接口时，执行 `.claude/commands/multi-player/SKILL.md` 的文档维护流程：先维护 API 原子文档，再同步聚合文档和 `docs/kn/总目录.md`。
4. 交付时列出实现的页面/保存链路、修改文件、验证结果和遗留待确认项。
