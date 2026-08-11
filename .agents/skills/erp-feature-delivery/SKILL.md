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
- `COMB`、`COMB_MULTI`、`CHECKBOX`、`RADIO_BTN` 可提供 `options`，格式为 `值:文案` 的逗号分隔字符串；生成的 `headList.itemList` 与列表筛选 `itemList` 必须解析为相同的 `FieldItem(value,text)`，不得保留原始字符串或输出空列表。`RADIO_BTN` 按 `COMB` 协议输出；`SWITCH` 也按 `COMB` 输出且 `itemList` 固定为 `1:开启`、`2:关闭`。所有选择数据字段（`USER`、`DEPT`、`BUSINESS`、`PRODUCT` 及其多选变体）必须提供目标 `businessCode`；后端返回的 `businessSelectConfig` 只能包含该 `businessCode`，禁止下发 URL、请求体、占位文案、标题、业务类型或单多选语义。前端依据 `businessCode` 常量注册表解析端点和展示语义。当选择字段配置了非空 `filterName` 时，列表筛选元数据也必须下发相同的仅编码配置。
- 业务选择回填属于**消费方业务模块**：每个具备新建/编辑表单的业务至多提供一个 `POST /erp/v1/{business}/selectionFill` 接口，接口以 `fieldAttr + referenceId` 识别来源；模块内按 `fieldAttr` 分派其全部上游业务的回填规则，禁止按上游单据新增 HTTP 接口。仅需选择、不需回填的字段不得调用该接口。字段设计可在 `BUSINESS` 字段声明 `selectionFill: true`；生成元数据保留该布尔值并下发为 `selectionFillConfig: { enabled: true }`，不得下发目标字段映射、来源 URL、SQL 或其他回填实现细节。`businessSelectConfig` 仍仅包含目标 `businessCode`。
- `selectionFill` 必须由当前模块应用服务经轻量跨模块 `*ReferenceQueryApi` / 查询 Port 按租户查询来源数据，返回仅包含当前表单可写 `attr` 路径的 `patch`。服务端必须校验 `fieldAttr` 是本模块已启用回填的选择字段、`referenceId` 属于当前租户且有效；正式保存必须按业务语义重新校验或重算受回填影响的快照字段，禁止信任前端 patch。
- 列表筛选元数据必须同时返回两类字段类型：`fieldType` 为源字段枚举值（与 `headList.fieldType` 一致，用于前端精确选择控件），`filterFieldType` 为筛选协议类型（用于 `conditions[].fieldType` 的白名单校验）。`ListCommonServiceImpl#filter` 统一派生二者：`COMB_MULTI`、`CHECKBOX` 映射为 `ENUM_MULTI`，`BUSINESS` 映射为 `BUSINESS`，`USER`、`DEPT` 映射为 `ID`；`NUM_INT`、`NUM_DOUBLE`、`AMOUNT`、`STOCK`、`DATE`、`TIME` 必须保留各自源类型。条件白名单、值长度校验和 Mapper SQL 必须同步支持这些操作符。
- `COMB`、`RADIO_BTN`、`SWITCH`、`COMB_MULTI`、`CHECKBOX` 的持久化筛选值统一按 JSON 处理：单选仅支持 `CONTAINS`、`NOT_CONTAINS`、`IS_EMPTY`、`IS_NOT_EMPTY`，多选额外支持 `CONTAINS_ALL`、`NOT_CONTAINS_ALL`；Mapper 对枚举条件必须使用 `JSON_CONTAINS`，禁止以 `LIKE` 匹配 JSON 文本。`DATE` 仅支持 `EQ`、`GE`、`LE`、`BETWEEN`、`IS_EMPTY`、`IS_NOT_EMPTY`；`TIME` 仅支持 `GE`、`LE`、`BETWEEN`、`IS_EMPTY`、`IS_NOT_EMPTY`。
- `DATE`、`TIME` 的筛选和保存传输值统一为 13 位毫秒时间戳；前端仅在控件展示层格式化为日期或年月日时分秒，后端不得要求 ISO 文本日期。
- `FILE`、`IMAGE`、`ADDRESS`、`SUB_ITEM`、`PRODUCT` 必须 `filterName: null`。子档字段也必须 `filterName: null`。
- `SUB_ITEM` 必须提供 `subFields`（允许显式为空）；父字段只出现在 `CREATE`/`UPDATE` 的 `headList`，装配为 `FieldEntity.subField`。子档的表名、聚合名、父子关联列、表单属性、Repository 查询和保存同步由 ROOT/CHILD 模块规格声明，禁止由字段 YAML 猜测。
- `listActions` 必须显式提供 `top`、`bottom`、`row`；无动作写空数组，不得默认添加新增或编辑按钮。

## 列表查询与持久化

- 业务列表接口统一直接接收 `ListBaseDTO`，不得为仅承载公共分页和动态筛选的列表重新生成 `*ListDTO`；Controller、总入口 Application Service 与 Query Application Service 的 `list` 签名必须一致。
- Query Application Service 固定执行 `AdminParamValidator.requireCorpid(dto)` → `ListQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap())` → 同一条件 Map 的 `findByCondition/count` → `ListBaseVO` 分页组装。禁止手工向条件 Map 写入 `id`、名称、字段、排序或分组片段；`schemaProvider` 必须从同一 `ListMetaProvider` 的筛选白名单派生元数据。
- `findByCondition` 和 `count` 必须共用同一个条件准备入口；Repository 实现必须显式 import 并调用 `ConditionMapHelper` 或统一的 `QueryConditionMapHelper`，不得绕过分页和筛选约束。

执行：

```bash
ruby .agents/skills/business-module-delivery/scripts/generate_field_metadata.rb <field-design.yaml> <field-metadata.json>
python3 .agents/skills/business-module-delivery/scripts/validate_field_metadata.py <field-metadata.json>
```

字段定义只能有一个可执行事实源。生成或维护 `*FieldEnum` 后，`*FieldFactory` 必须按场景、选项、选择目标和子档定义派生 `headList`，`*ListMetaProvider` 必须从同一字段定义派生表头、筛选属性、白名单列、操作符、筛选选项和选择目标；禁止再维护并行的 `DEFINITIONS`、字段列表或筛选符号常量。生成后不得保留 `emptyList`/`emptyMap`/`List.of()`/`Map.of()` 形式的空筛选、空表头或空条件元数据实现。

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
- 正式保存进入 Repository 前，应用服务必须统一覆盖维护 `creatorId`、`modifyId`（均为 `dto.userId`）、`addTime`、`updateTime`（同一 `now`）和 `del=0`；持久化领域模型、DTO、转换器与 Mapper 参数统一使用属性名 `del`，禁止业务层使用兼容别名 `deleted`，数据库列仍为 `del`。
- 当业务表主键为数据库 `AUTO_INCREMENT` 时，新增保存不得生成、预填或覆盖 `id`，也不得依赖 `xbb-erp-base-idgen`；Repository 插入前实体 `id` 必须为 `null`，由数据库生成后回写。仅更新既有记录时允许从请求/已存记录携带 `id`。受影响模块测试必须断言插入实体的 `id` 为 `null`。
- 删除草稿缓存只能发生在正式保存全部成功之后，并且仅删除请求 `draftMeta.draftCode` 指定的同租户草稿。

## Spring 装配与跨模块引用注意事项

- 注册到公共 Registry 的列表渲染 Provider、选择 Provider 等 Bean，不得反向依赖该 Registry、Renderer，或其调用链上的页面 Query Application Service；否则容易形成 `Registry -> Provider -> QueryService -> Renderer -> Registry` 循环依赖。
- 跨模块引用查询契约（如 `*ReferenceQueryApi`）必须由独立的轻量查询 Application Service 实现，该实现只依赖 Repository 或明确的查询 Port。禁止由同时注入 `ListValueRenderer`、Provider Registry 或页面编排依赖的列表 Query Application Service 实现。
- 新增 Provider、让 Query Application Service 接入 Renderer，或调整 `*ReferenceQueryApi` 实现前，必须检查构造器依赖图：Provider 到引用查询实现的路径不得回流到 Renderer 或 Registry。禁止通过 `spring.main.allow-circular-references=true` 规避问题。
- 受影响模块必须增加或更新最小 Spring `ApplicationContext` 回归测试，至少注册 Provider、Registry、Renderer、引用查询服务和列表 Query Application Service，并断言 Context 可以成功 `refresh`，以防止运行时才暴露循环依赖。

## 实现与交付门禁

1. 先执行 ROOT/CHILD 规格 `dry-run`；确认模块目录不含短横线、源码路径由 `packageBase` 推导且与 `package` 声明完全一致；再检查 `admin -> application -> domain` 依赖、Repository 边界、事务和子档批量加载。
2. `addItem` 以 `CREATE` 字段生成空表单与 `headList`；`updateItem` 以 `UPDATE` 字段生成 `headList`，并按明确关联回填主档、子档和 `sectionState`。每个选择数据字段必须在两个场景的 `headList` 中组装只含目标 `businessCode` 的 `businessSelectConfig`；不得下发任何 URL、租户请求体或展示语义。选项字段必须返回非空 `itemList`。
3. 列表筛选必须只接受由 `filterName` 派生的属性/列/操作符白名单，绝不接受前端传入 SQL、列名或操作符；可筛选字段必须返回源 `fieldType` 加协议 `filterFieldType`，`BUSINESS(16)` 的协议类型为 `BUSINESS`，`USER(12)`、`DEPT(14)` 为 `ID`，三者均返回仅含目标编码的 `businessSelectConfig`；选项字段必须复用表单 `itemList`。验证单选/多选 JSON 枚举条件使用 `JSON_CONTAINS`，`DATE`、`TIME` 的白名单严格符合字段元数据脚本生成的操作符集合。
4. 检查 Controller、Application Service、Query Application Service 的列表签名均为 `ListBaseDTO`；Query Application Service 使用 `ListQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap())`，`findByCondition` 与 `count` 共享结果 Map，Repository 已 import 并调用条件准备工具，字段工厂不保留无意义的 `SceneFieldMeta.class::cast`。
5. 检查草稿缓存仓储及其基础设施实现、`DraftSaveVO` 返回类型、草稿保存的协议/通用校验、草稿列表缓存读取、业务校验器占位调用，以及正式保存成功后的草稿缓存删除。
6. 为字段场景、下拉选项、选择字段的仅 `businessCode` 配置、`SUB_ITEM.subField`、筛选映射、列表动作、草稿缓存和保存校验链补充受影响模块测试；验证 `ListMetaProvider` 的筛选、表头和条件元数据均非空且来自同一字段定义。
   涉及 `selectionFill` 时，额外验证：启用字段在 CREATE/UPDATE `headList` 下发 `selectionFillConfig.enabled=true`，未启用选择字段不下发该配置；每个上游字段均由同一模块接口按 `fieldAttr` 正确分派；无效字段、跨租户或失效引用被拒绝；保存校验拒绝伪造或过期回填快照。
7. 执行 `scripts/harness-verify.sh`；新模块额外执行 `scripts/harness-verify.sh --module xbb-erp-module-<name>`，并运行受影响 Maven 模块测试。
8. 接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航；交付报告列出变更、验证、迁移和待确认项。
9. 不提交真实凭据、私钥或 Token；不绕过失败的 Harness 或 CI 检查。
10. 涉及 Provider、Registry、Renderer 或跨模块 `*ReferenceQueryApi` 时，检查 Bean 构造器依赖图不存在回流闭环，并运行最小 Spring Context 回归测试。
