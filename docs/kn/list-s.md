# list-s

## 客户
- 接口：`POST /erp/v1/customer/list`
- 用途：客户列表查询
- 差异点：支持 `keyword + conditions` 动态筛选协议
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-list.md`

## 供应商
- 接口：`POST /erp/v1/supplier/list`
- 用途：供应商列表查询
- 差异点：当前由 `SupplierListDTO` 直接承接字段筛选，并兼容 `conditions` 动态条件列表
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-list.md`

## 公共列表元数据
- 接口：`POST /erp/v1/common/list/filter`
- 用途：按业务编码返回筛选字段元数据
- 差异点：仅返回筛选字段定义，不返回表头和按钮
- 业务文档：`docs/kn/common-m.md`
- API 文档：`docs/api/endpoints/common-filter.md`

## 公共列表元数据
- 接口：`POST /erp/v1/common/list/header`
- 用途：按业务编码返回表头字段元数据
- 差异点：仅返回表头字段定义，字段包含 `required`、`editable`
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
