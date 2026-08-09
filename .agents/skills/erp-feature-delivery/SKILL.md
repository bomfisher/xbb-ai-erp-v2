---
name: erp-feature-delivery
description: 在本仓库交付或重构完整 ERP 功能，编排需求规格、DDD 模块、字段 YAML、列表/表单元数据、主子档保存、代码生成、测试、Flyway 与接口文档。用于新增业务模块、跨层功能修改，或需要消除字段配置重复和筛选白名单硬编码的任务。
---

# ERP 功能交付

以 `xbb-ai-erp-v2` 后端仓库的 Harness 作为功能开发的唯一执行路径。开始前先进入该目录；本文中的 `docs/`、`.claude/`、`scripts/` 路径均相对于该后端仓库。先读 `docs/harness/README.md`，再按需求选择引用的专业 SKILL。

## 开始与输入

1. 阅读 `docs/harness/README.md`、`docs/harness/工程规则唯一事实源.md`、`docs/harness/文档地图.md`、`docs/harness/功能交付输入模板.md` 与目标模块/API 文档。
2. 检查 `git status --short`；由开发者决定是否创建 worktree，禁止覆盖既有改动。
3. 明确主聚合、从聚合、数据库关系、页面场景、筛选白名单、列表动作、保存/草稿语义与验收标准。缺失业务事实时暂停确认，禁止按字段名猜测业务规则。
4. 新模块的目录后缀和 `moduleCode` 只能使用小写字母、数字、下划线，禁止使用短横线；Java `packageBase` 的每个片段必须是合法 Java 标识符。生成前确认 `xbb-erp-module-<moduleCode>`、`src/main/java/<packageBase 路径>` 和所有生成文件的 `package` 声明一致，禁止用模块目录名替代 Java 包路径。

## 字段元数据优先

在实现 `list`、`addItem`、`updateItem` 或动态表单前，先编写并校验字段设计 YAML：

```yaml
businessCode: DEMO
fields:
  - name: name
    attr: main.name
    attrName: 名称
    fieldType: TEXT
    scenes: [LIST, CREATE, UPDATE]
    required: true
    editable: true
    defaultValue: null
    filterName: name
listActions:
  top: []
  bottom: []
  row: []
```

- 每个字段必须显式提供 `name`、`attr`、`attrName`、`fieldType`、`scenes`、`required`、`editable`、`defaultValue`、`filterName`；`filterName: null` 表示不可筛选。
- `name` 是稳定设计标识/枚举常量来源，`attr` 是前端提交和回填路径，`filterName` 是服务端数据库列白名单；三者不得混用。
- `COMB`、`COMB_MULTI`、`CHECKBOX`、`RADIO_BTN` 可提供 `options`；`BUSINESS` 必须提供上游 `businessCode`。`USER(12)` 与 `DEPT(14)` 必须提供指向组织模块成员或部门选择接口的 `businessSelectConfig`，包含当前租户、快捷搜索、弹窗搜索、按 ID 回显地址、占位文案、弹窗标题和单/多选语义；当三类选择字段配置了非空 `filterName` 时，列表筛选元数据必须下发与表单相同的配置，以便先选择关联数据再提交筛选。`BUSINESS` 的筛选协议类型固定为 `BUSINESS`，`USER`、`DEPT` 固定为 `ID`，符号集合均由 `fieldType` 的统一映射推导，禁止在业务 Provider 逐字段硬编码。
- `FILE`、`IMAGE`、`ADDRESS`、`SUB_ITEM`、`PRODUCT` 必须 `filterName: null`。子档字段也必须 `filterName: null`。
- `SUB_ITEM` 必须提供 `subFields`（允许显式为空）；父字段只出现在 `CREATE`/`UPDATE` 的 `headList`，装配为 `FieldEntity.subField`。子档的表名、聚合名、父子关联列、表单属性、Repository 查询和保存同步由 ROOT/CHILD 模块规格声明，禁止由字段 YAML 猜测。
- `listActions` 必须显式提供 `top`、`bottom`、`row`；无动作写空数组，不得默认添加新增或编辑按钮。

## 列表查询与持久化

- 业务列表接口统一直接接收 `ListBaseDTO`，不得为仅承载公共分页和动态筛选的列表重新生成 `*ListDTO`。
- 列表 Application Service 传给 Repository 的查询条件只能包含 `conditions`、`pageSize` 和 `offset`；业务筛选字段必须由 `*ListQueryAdapter` 将 `conditions` 按字段元数据白名单映射，禁止把 `id`、名称、排序片段、分组片段或前端原始字段直接塞入条件 Map。
- `findByCondition` 和 `count` 必须共用同一个条件准备入口；Repository 实现必须显式 import 并调用 `ConditionMapHelper` 或统一的 `QueryConditionMapHelper`，不得绕过分页和筛选约束。

执行：

```bash
ruby .agents/skills/business-module-delivery/scripts/generate_field_metadata.rb <field-design.yaml> <field-metadata.json>
python3 .agents/skills/business-module-delivery/scripts/validate_field_metadata.py <field-metadata.json>
```

字段定义只能有一个可执行事实源。生成或维护 `*FieldEnum` 后，`*FieldFactory` 必须按场景、选项和子档定义派生 `headList`，`*ListMetaProvider` 必须从同一字段定义派生表头、筛选属性、白名单列和操作符；禁止再维护并行的 `DEFINITIONS`、字段列表或筛选符号常量。

## 实施路由

- 新业务模块：使用 `.claude/commands/init-module/SKILL.md`，先准备 YAML 规格并执行 `dry-run`；随后使用 `$ddd-business-module-development` 完成业务用例。
- 列表、新建、编辑、保存或草稿：使用 `$ddd-business-module-development`，先完成字段元数据，再实现公共列表、场景字段和主子档保存协议。
- 接口新增或契约变化：完成验证后执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航。
- 数据库结构变化：先获得历史库基线确认，再遵守 `docs/harness/数据库迁移规范.md` 新增 Flyway 迁移。

## 保存与草稿强制协议

- 所有 ERP 模块都必须通过 `application.port` 定义草稿缓存仓储，并在 `infrastructure` 提供缓存实现；禁止以空实现、内存临时对象、业务表或“暂不支持”异常替代草稿能力。
- `saveDraft` 固定返回 `xbb.ai.erp.base.common.vo.DraftSaveVO`。流程固定为：DTO 转草稿保存上下文 → 协议校验 → `CommonValidator.validateForDraft` → 转草稿 Pojo → 缓存仓储保存 → 回写 `draftMeta.draftCode` → 返回 `DraftSaveVO.draftCode`。
- `draftList` 只能从草稿缓存读取，不查询正式业务表；按 `corpid` 和明确上限读取后转换为草稿列表 VO。`loadDraft` 也只能经草稿缓存按租户和草稿编码读取。
- `saveAndSubmit` 固定按“协议校验 → 通用校验 → 业务校验 → 事务内正式保存 → 成功后删除来源草稿缓存”执行。每个模块必须生成 `*SaveBusinessValidator` 及 `validateForSubmit` 方法；暂未定义的领域规则可保留空占位方法，但不得跳过调用。
- 删除草稿缓存只能发生在正式保存全部成功之后，并且仅删除请求 `draftMeta.draftCode` 指定的同租户草稿。

## 实现与交付门禁

1. 先执行 ROOT/CHILD 规格 `dry-run`；确认模块目录不含短横线、源码路径由 `packageBase` 推导且与 `package` 声明完全一致；再检查 `admin -> application -> domain` 依赖、Repository 边界、事务和子档批量加载。
2. `addItem` 以 `CREATE` 字段生成空表单与 `headList`；`updateItem` 以 `UPDATE` 字段生成 `headList`，并按明确关联回填主档、子档和 `sectionState`。`BUSINESS(16)`、`USER(12)`、`DEPT(14)` 字段必须在两个场景的 `headList` 中组装 `businessSelectConfig`；前者至少包含上游 `businessType/businessCode`，后两者必须指向组织模块选择接口。三者均需携带当前租户 `requestPayload`、完整查询地址、占位文案、弹窗标题和单/多选语义，禁止只输出字段类型。
3. 列表筛选必须只接受由 `filterName` 派生的属性/列/操作符白名单，绝不接受前端传入 SQL、列名或操作符；可筛选的 `BUSINESS(16)` 字段必须使用独立的 `BUSINESS` 协议字段类型，`USER(12)`、`DEPT(14)` 使用 `ID` 协议字段类型，三者均返回 `businessSelectConfig`。
4. 检查列表直接使用 `ListBaseDTO`、条件 Map 只含 `conditions`/`pageSize`/`offset`、Repository 已 import 并调用条件准备工具，字段工厂不保留无意义的 `SceneFieldMeta.class::cast`。
5. 检查草稿缓存仓储及其基础设施实现、`DraftSaveVO` 返回类型、草稿保存的协议/通用校验、草稿列表缓存读取、业务校验器占位调用，以及正式保存成功后的草稿缓存删除。
6. 为字段场景、下拉选项、`BUSINESS(16)` 选择配置、`SUB_ITEM.subField`、筛选映射、列表动作、草稿缓存和保存校验链补充受影响模块测试。
7. 执行 `scripts/harness-verify.sh`；新模块额外执行 `scripts/harness-verify.sh --module xbb-erp-module-<name>`，并运行受影响 Maven 模块测试。
8. 接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航；交付报告列出变更、验证、迁移和待确认项。
9. 不提交真实凭据、私钥或 Token；不绕过失败的 Harness 或 CI 检查。
