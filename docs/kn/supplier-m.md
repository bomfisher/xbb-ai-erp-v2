# supplier-m.md

## 列表

### `list`
- 接口路径：`POST /erp/v1/supplier/list`
- 用途：供应商列表查询
- 差异点：当前由 `SupplierListDTO` 直接承接字段筛选，并兼容 `conditions` 动态条件列表
- API 文档：`docs/api/endpoints/supplier-list.md`

## 新建

### `addItem`
- 接口路径：`POST /erp/v1/supplier/addItem`
- 用途：供应商新增表单初始化
- 差异点：返回主档、子档与 `sectionState` 的完整抽屉初始化结构，并将 `供应商分类`、`主营业务分类`、`业务状态`、`引用状态` 下发为固定枚举，将 `归属采购` 下发为成员单选 `USER(12)`
- API 文档：`docs/api/endpoints/supplier-add-item.md`

## 编辑

### `updateItem`
- 接口路径：`POST /erp/v1/supplier/updateItem`
- 用途：供应商编辑表单加载
- 差异点：按现有供应商数据回填主档、子档与 `sectionState`，并沿用新增接口的 `COMB(8)` / `USER(12)` 字段元数据语义
- API 文档：`docs/api/endpoints/supplier-update-item.md`

## 保存

### `save`
- 接口路径：`POST /erp/v1/supplier/save`
- 用途：供应商正式保存
- 差异点：旧保存接口直接返回供应商主键
- API 文档：`docs/api/endpoints/supplier-save.md`

### `saveAndSubmit`
- 接口路径：`POST /erp/v1/supplier/saveAndSubmit`
- 用途：供应商正式保存并提交
- 差异点：执行严格校验并可清理草稿
- API 文档：`docs/api/endpoints/supplier-save-and-submit.md`

## 草稿

### `saveDraft`
- 接口路径：`POST /erp/v1/supplier/saveDraft`
- 用途：供应商草稿保存
- 差异点：返回草稿编码，用于后续续编
- API 文档：`docs/api/endpoints/supplier-save-draft.md`

### `draftList`
- 接口路径：`POST /erp/v1/supplier/draftList`
- 用途：供应商草稿列表查询
- 差异点：按更新时间倒序返回最近 `10` 条草稿摘要
- API 文档：`docs/api/endpoints/supplier-draft-list.md`

### `loadDraft`
- 接口路径：`POST /erp/v1/supplier/loadDraft`
- 用途：供应商草稿加载
- 差异点：返回完整编辑态数据与草稿元信息
- API 文档：`docs/api/endpoints/supplier-load-draft.md`

## 详情

### `detail`
- 接口路径：`POST /erp/v1/supplier/detail`
- 用途：供应商详情查询
- 差异点：返回 `mainData` 包裹的详情主体
- API 文档：`docs/api/endpoints/supplier-detail.md`

## 删除

### `delete`
- 接口路径：`POST /erp/v1/supplier/delete`
- 用途：供应商批量删除
- 差异点：成功时返回空业务数据体
- API 文档：`docs/api/endpoints/supplier-delete.md`
