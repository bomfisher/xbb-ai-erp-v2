---
name: erp-feature-delivery
description: 在本仓库交付或重构完整 ERP 功能，编排需求规格、DDD 模块、字段 YAML、列表/表单元数据、主子档保存、代码生成、测试、Flyway 与接口文档。用于新增业务模块、跨层功能修改，或需要消除字段配置重复和筛选白名单硬编码的任务。
---

# ERP 功能交付

以仓库 Harness 作为功能开发的唯一执行路径。先读 `docs/harness/README.md`，再按需求选择引用的专业 SKILL。

## 开始与输入

1. 阅读 `docs/harness/README.md`、`docs/harness/工程规则唯一事实源.md`、`docs/harness/文档地图.md`、`docs/harness/功能交付输入模板.md` 与目标模块/API 文档。
2. 检查 `git status --short`；由开发者决定是否创建 worktree，禁止覆盖既有改动。
3. 明确主聚合、从聚合、数据库关系、页面场景、筛选白名单、列表动作、保存/草稿语义与验收标准。缺失业务事实时暂停确认，禁止按字段名猜测业务规则。

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
- `COMB`、`COMB_MULTI`、`CHECKBOX`、`RADIO_BTN` 可提供 `options`；`BUSINESS` 可提供上游 `businessCode`。`symbols` 和列表筛选协议类型必须由 `fieldType` 的统一映射推导，禁止在业务 Provider 逐字段硬编码。
- `FILE`、`IMAGE`、`ADDRESS`、`SUB_ITEM`、`PRODUCT` 必须 `filterName: null`。子档字段也必须 `filterName: null`。
- `SUB_ITEM` 必须提供 `subFields`（允许显式为空）；父字段只出现在 `CREATE`/`UPDATE` 的 `headList`，装配为 `FieldEntity.subField`。子档的表名、聚合名、父子关联列、表单属性、Repository 查询和保存同步由 ROOT/CHILD 模块规格声明，禁止由字段 YAML 猜测。
- `listActions` 必须显式提供 `top`、`bottom`、`row`；无动作写空数组，不得默认添加新增或编辑按钮。

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

## 实现与交付门禁

1. 先执行 ROOT/CHILD 规格 `dry-run`；再检查 `admin -> application -> domain` 依赖、Repository 边界、事务和子档批量加载。
2. `addItem` 以 `CREATE` 字段生成空表单与 `headList`；`updateItem` 以 `UPDATE` 字段生成 `headList`，并按明确关联回填主档、子档和 `sectionState`。
3. 列表筛选必须只接受由 `filterName` 派生的属性/列/操作符白名单，绝不接受前端传入 SQL、列名或操作符。
4. 为字段场景、下拉选项、`SUB_ITEM.subField`、筛选映射和列表动作补充受影响模块测试。
5. 执行 `scripts/harness-verify.sh`；新模块额外执行 `scripts/harness-verify.sh --module xbb-erp-module-<name>`，并运行受影响 Maven 模块测试。
6. 接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和导航；交付报告列出变更、验证、迁移和待确认项。
7. 不提交真实凭据、私钥或 Token；不绕过失败的 Harness 或 CI 检查。
