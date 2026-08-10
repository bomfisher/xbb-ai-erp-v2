# common-m.md

## 列表

### 公共列表筛选字段

- 接口：`POST /erp/v1/common/list/filter`
- 用途：按业务编码查询列表筛选字段元数据
- 差异点：只返回筛选字段定义，包含 `supportedSymbols`、`itemList`，并可按字段返回 `businessSelectConfig`
- API 文档：`docs/api/endpoints/common-filter.md`

### 公共列表表头字段

- 接口：`POST /erp/v1/common/list/header`
- 用途：按业务编码查询列表表头字段元数据
- 差异点：只返回表头字段定义，字段包含 `required`、`editable`、`itemList`，并可按字段返回 `businessSelectConfig`；列表专用字段可返回 `renderValueAttr` 指向原始码值属性。
- API 文档：`docs/api/endpoints/common-header.md`

### 公共列表顶部按钮

- 接口：`POST /erp/v1/common/list/topButton`
- 用途：按业务编码查询列表顶部按钮元数据
- 差异点：只返回页面头部操作区按钮元数据
- API 文档：`docs/api/endpoints/common-top-button.md`

### 公共列表底部按钮

- 接口：`POST /erp/v1/common/list/bottomButton`
- 用途：按业务编码查询列表底部按钮元数据
- 差异点：只返回页面底部操作区按钮元数据
- API 文档：`docs/api/endpoints/common-bottom-button.md`

### 公共列表行内动作

- 接口：`POST /erp/v1/common/list/rowAction`
- 用途：按业务编码查询列表行内动作元数据
- 差异点：只返回行内动作定义，当前客户列表仅返回 `EDIT`
- API 文档：`docs/api/endpoints/common-row-action.md`
