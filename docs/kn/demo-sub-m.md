# demo-sub-m

## 列表与表单
- 列表：`POST /erp/v1/demo-sub/list`；API：`docs/api/endpoints/demo-sub-list.md`
- 新建：`POST /erp/v1/demo-sub/addItem`；API：`docs/api/endpoints/demo-sub-add-item.md`
- 编辑：`POST /erp/v1/demo-sub/updateItem`；API：`docs/api/endpoints/demo-sub-update-item.md`
- 表单关联：`dataId` 在新建和编辑态均下发 `DEMO` 的 `BUSINESS(16)` 选择配置。
- 列表筛选：`dataId` 下发同一 `DEMO` 选择配置，选择关联数据后按 `data_id` 筛选；元数据同时下发源 `fieldType` 和查询协议 `filterFieldType`。
- 成员与部门：`userId`、`departmentId` 在列表筛选和新建/编辑中使用组织模块选择器，并保存稳定 ID。
- 列表展示值：服务端依据 LIST 表头批量渲染 Demo、成员和部门名称；同一页所有同目标引用值合并去重后仅查询一次。

## 保存与草稿
- 正式保存：`POST /erp/v1/demo-sub/saveAndSubmit`；API：`docs/api/endpoints/demo-sub-save-and-submit.md`
- 草稿接口：`saveDraft`、`draftList`、`loadDraft` 的协议已生成，但尚未配置草稿存储实现。
