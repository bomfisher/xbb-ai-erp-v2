# common-list-s.md

## 公共列表元数据

- 接口：`POST /erp/v1/common/list/filter`
- 用途：按业务编码返回筛选字段元数据
- 差异点：仅返回筛选字段定义，不返回表头和按钮；字段可按需附带 `businessSelectConfig`
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-filter.md`

## 公共列表元数据

- 接口：`POST /erp/v1/common/list/header`
- 用途：按业务编码返回表头字段元数据
- 差异点：仅返回表头字段定义，字段包含 `required`、`editable`，并可附带 `itemList` 与 `businessSelectConfig`
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-header.md`

## 公共列表元数据

- 接口：`POST /erp/v1/common/list/topButton`
- 用途：按业务编码返回顶部按钮元数据
- 差异点：仅返回页面顶部操作区按钮定义
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-top-button.md`

## 公共列表元数据

- 接口：`POST /erp/v1/common/list/bottomButton`
- 用途：按业务编码返回底部按钮元数据
- 差异点：仅返回页面底部操作区按钮定义
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-bottom-button.md`

## 公共列表元数据

- 接口：`POST /erp/v1/common/list/rowAction`
- 用途：按业务编码返回行内动作元数据
- 差异点：当前客户列表仅落地 `EDIT` 行动作，不提前暴露其他动作
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-row-action.md`
