---
name: business-module-delivery
description: 按统一 DDD 结构交付任意 ERP 业务模块的列表、新建、编辑、草稿和提交保存能力。适用于需要实现 list、addItem、updateItem、saveDraft、saveAndSubmit、draftList、loadDraft 接口的主表或主从表业务。
---

# 业务模块标准交付

本 Skill 只定义跨业务稳定的页面与保存交付契约。字段、状态机、来源校验、库存、应付、审批、幂等和其他领域规则由目标业务的模块文档与需求决定，禁止写入本 Skill。

## 开始前

1. 阅读 `docs/harness/README.md`、`docs/harness/工程规则唯一事实源.md`、`docs/harness/文档地图.md`、`docs/guide/DDD业务模块开发指南.md`、`docs/guide/业务模块目录规范.md`，以及目标模块和相关 API 原子文档。
2. 执行 `git status --short`，保护已有改动；先由用户决定是否创建 worktree。
3. 以 `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java` 的 HTTP 边界为唯一接口形态参考。
4. 从开发设计、API 原子文档和已确认的字段设计整理字段元数据输入；缺少字段名称、字段类型、场景、`filterName`、列表动作或业务编码时，先向开发者确认，禁止按列名、Java 类型或其他模块猜测。

## 字段元数据输入

ROOT 在生成代码前必须提供 `field-metadata.json`。该文件只能转录已经确认的输入事实，不能由 Agent 填充默认业务值：

```json
{
  "businessCode": "SALES_ORDER",
  "fields": [
    {
      "name": "orderNo",
      "attr": "main.orderNo",
      "attrName": "订单编号",
      "fieldType": "TEXT",
      "scenes": ["LIST", "CREATE", "UPDATE"],
      "filterName": "order_no"
    }
  ],
  "listActions": {
    "top": [{"actionCode": "ADD", "actionName": "新增"}],
    "bottom": [],
    "row": [{"actionCode": "EDIT", "actionName": "编辑"}]
  }
}
```

- `businessCode` 是 `BusinessCodeEnum` 的显式枚举值；若基础枚举不存在该值，先由开发者确认允许修改公共模块，不能自行发明。
- 每个字段必须提供 `attr`、`attrName`、`fieldType` 和使用场景；`filterName` 是服务端筛选白名单列名，填 `null` 表示不可筛选。
- 筛选协议类型和 `symbols` 始终由 `fieldType` 的统一映射推导，设计文档和字段元数据不得重复维护；`FILE`、`IMAGE`、`ADDRESS`、`SUB_ITEM`、`PRODUCT` 必须使用 `filterName: null`。
- 没有 LIST、CREATE、UPDATE、筛选或按钮需求时，输入中必须显式写为空数组或 `filterName: null`；“未提供”不等于“不需要”。

## 聚合与目录边界

- 单主表或主从表的主表使用 `ROOT` 规格：可生成 `admin`、`application`、`domain`、`infrastructure.persistence` 和 Mapper XML 骨架。
- 主从表的从表使用 `CHILD` 规格：仅生成 `domain`、`infrastructure.persistence` 和 Mapper XML；禁止创建独立 Controller、DTO、VO、Application、列表或页面。
- 按 `docs/guide/业务模块目录规范.md` 按需建立目录，不能为了凑骨架创建空包。实现本 Skill 的七个接口时，必须具备其所需的 `admin`、`application`、`domain`、`infrastructure.persistence` 与 Mapper XML 职责目录。
- 主档与子档的字段、关系、同步与删除语义由目标业务定义；应用服务在同一事务边界内编排主子档保存。

## 必须交付的 HTTP 接口

主表 Controller 的路径为 `/erp/v1/{business}`，仅通过总入口 Application Service 调用，并使用 `ResultVO.success()` 包装：

| 路径 | 入参 | 返回 |
| --- | --- | --- |
| `POST /list` | `ListBaseDTO` | `ListBaseVO<*ListItemVO>` |
| `POST /addItem` | `BaseDTO` | `SaveItemVO<*SaveItemVO>` |
| `POST /updateItem` | `IdBaseDTO` | `SaveItemVO<*SaveItemVO>` |
| `POST /saveDraft` | `*DraftSaveDTO` | `*DraftSaveVO` |
| `POST /saveAndSubmit` | `*SubmitSaveDTO` | `BaseVO` |
| `POST /draftList` | `*DraftListDTO` | `List<*DraftListItemVO>` |
| `POST /loadDraft` | `*DraftLoadDTO` | `*DraftDetailVO` |

- `list` 的 Controller、总入口 Application Service 与 Query Application Service 均直接使用 `ListBaseDTO`，不得仅为公共分页、关键词和动态 `conditions` 创建 `*ListDTO`。Query Application Service 必须执行 `requireCorpid`、`ListQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap())`，并将同一个条件 Map 交给 `findByCondition` 和 `count`；`schemaProvider` 从 `*ListMetaProvider` 的筛选白名单派生，禁止手工拼接字段筛选、排序或分组条件。列表需要子档摘要时必须批量查询。
- `addItem` 返回 `CREATE` 场景字段元数据与空表单；`updateItem` 返回 `UPDATE` 元数据以及主档、子档和 `sectionState` 回填。
- `saveDraft`、`draftList`、`loadDraft` 放在 `application.service.draft`；草稿仓储定义在 `application.port`，实现放在基础设施层。
- `saveAndSubmit` 放在 `application.service.save`，按“协议校验 → 通用字段校验 → 业务校验 → 主子档同步”执行；只有正式保存成功才删除来源草稿。

## 执行步骤

1. 用 `scripts/validate_field_metadata.py` 校验字段元数据来源完整；输出缺失项时暂停并向开发者确认。
2. 用 `scripts/build_delivery_scope.py` 明确模块、主表、可选从表和字段元数据；在业务设计中单独记录领域特有规则。
3. 用 `scripts/validate_module_specs.py` 校验 ROOT/CHILD 角色、模块一致性和生成范围；先运行 `scripts/run_codegen.py` 的 dry-run。
4. 用户确认后才使用 `scripts/run_codegen.py --apply`。ROOT 必须生成 `*FieldEnum`、场景字段提供者、`headList`、`BusinessCodeEnum` 对应值和包含实际字段白名单/动作的 `*ListMetaProvider`；不得生成空 Provider。
5. 用 `scripts/verify_module_delivery.py` 校验模块目录职责、Mapper 注册、ROOT 的七个接口、统一 `ListBaseDTO + ListQueryMapUtil + schemaProvider` 列表链路、字段元数据、`headList`、业务编码和 CHILD 无独立 HTTP/Application 层。
6. 接口契约变化时，执行 `.claude/commands/multi-player/SKILL.md`，维护 API 原子文档、聚合文档和 `docs/kn/总目录.md`。
7. 运行 `scripts/harness-verify.sh`、目标模块测试和本 Skill 脚本测试。

## 脚本

- `scripts/build_delivery_scope.py --module-code <domain> --root-spec <主表.yaml> --field-metadata <field-metadata.json> [--child-spec <从表.yaml> ...] --output <scope.json>`
- `scripts/generate_field_metadata.rb <字段设计.yaml> <field-metadata.json>`
- `scripts/validate_field_metadata.py <field-metadata.json>`
- `scripts/validate_module_specs.py <主表.yaml> [从表.yaml ...]`
- `scripts/run_codegen.py --project-root . <规格.yaml> [规格.yaml ...] [--apply]`
- `scripts/verify_mapper_registration.py <模块目录>`
- `scripts/verify_module_delivery.py <模块目录> <主聚合名> --field-metadata <field-metadata.json> [--child <从聚合名> ...] [--skip-tests]`
- `scripts/test_delivery_scripts.py`
