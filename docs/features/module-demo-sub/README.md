# DEMO_SUB 功能交付

## 业务边界

- `demo_sub` 是独立的下游业务模块，对外业务编码为 `DEMO_SUB`。
- `data_id` 必须关联同一租户下的 `demo.id`；本期仅保存关联 ID，不负责创建或维护 `demo` 主档。
- `name` 是必填名称；创建人和修改人由服务端审计上下文维护，不接受表单编辑。
- 列表元数据不下发顶部、底部或行操作；前端仍支持按后端元数据展示列表和加载动态表单。

## 数据事实

- 表 `demo_sub` 已由 `V3__create_demo_module_tables.sql` 创建，本模块复用该表，不重复新增 Flyway 迁移。
- ROOT 规格：`specs/demo-sub.yaml`
- 字段设计：`specs/field-design.yaml`
- 字段元数据：`specs/field-metadata.json`

## 接口范围

- `/erp/v1/demo-sub/list`
- `/erp/v1/demo-sub/addItem`
- `/erp/v1/demo-sub/updateItem`
- `/erp/v1/demo-sub/saveDraft`
- `/erp/v1/demo-sub/saveAndSubmit`
- `/erp/v1/demo-sub/draftList`
- `/erp/v1/demo-sub/loadDraft`
