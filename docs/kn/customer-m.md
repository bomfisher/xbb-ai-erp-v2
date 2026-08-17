# customer-m.md

## 列表

### `list`
- 接口路径：`POST /erp/v1/customer/list`
- 用途：客户列表查询
- 差异点：支持 `keyword + conditions` 动态筛选新协议
- API 文档：`docs/api/endpoints/customer-list.md`

## 新建

### `addItem`
- 接口路径：`POST /erp/v1/masterData/customer/addItem`
- 用途：客户新增表单元数据初始化
- 差异点：联系人以顶层 `contacts` 的 `SUB_ITEM(49)` 子表协议返回
- API 文档：`docs/api/endpoints/customer-add-item.md`

## 编辑

### `updateItem`
- 接口路径：`POST /erp/v1/masterData/customer/updateItem`
- 用途：客户编辑表单元数据加载
- 差异点：回填顶层 `contacts` 子表，保留联系人行 ID 供保存同步
- API 文档：`docs/api/endpoints/customer-update-item.md`

## 保存

### `saveAndSubmit`
- 接口路径：`POST /erp/v1/masterData/customer/saveAndSubmit`
- 用途：客户正式保存并提交
- 差异点：事务内同步联系人子表并回写默认联系人 ID
- API 文档：`docs/api/endpoints/customer-save-and-submit.md`

## 草稿

### `saveDraft`
- 接口路径：`POST /erp/v1/customer/saveDraft`
- 用途：客户草稿保存
- 差异点：执行宽松校验，仅写入草稿存储
- API 文档：`docs/api/endpoints/customer-save-draft.md`

### `draftList`
- 接口路径：`POST /erp/v1/customer/draftList`
- 用途：客户草稿列表查询
- 差异点：返回最近 `10` 条草稿摘要，不暴露数据库 `id`
- API 文档：`docs/api/endpoints/customer-draft-list.md`

### `loadDraft`
- 接口路径：`POST /erp/v1/customer/loadDraft`
- 用途：客户草稿加载
- 差异点：返回完整编辑态数据与 `sectionState`
- API 文档：`docs/api/endpoints/customer-load-draft.md`

## 业务选择

### `businessSelect`
- 接口路径：`POST /erp/v1/masterData/customer/businessSelect/quickSearch|dialogSearch|getById`
- 用途：为销售订单和销售出库等业务提供客户选择与已有值回显。
- API：`docs/api/endpoints/master-data-customer-business-select-quick-search.md`
- API：`docs/api/endpoints/master-data-customer-business-select-dialog-search.md`
- API：`docs/api/endpoints/master-data-customer-business-select-get-by-id.md`
