# edit-s

## 客户
- 接口：`POST /erp/v1/customer/updateItem`
- 用途：客户编辑表单元数据加载
- 差异点：按已有客户数据回填子档与 `sectionState`
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-update-item.md`

## 供应商
- 接口：`POST /erp/v1/supplier/updateItem`
- 用途：供应商编辑表单加载
- 差异点：按现有供应商数据回填主档、子档与 `sectionState`
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-update-item.md`

## 采购
- 接口：`POST /erp/v1/purchase/request/updateItem`、`POST /erp/v1/purchase/order/updateItem`
- 用途：采购申请、采购订单编辑表单加载
- 差异点：回填的子档数组由 `fieldType=50` 容器的 `attr` 动态对齐，列定义位于 `subField`
- 业务文档：`docs/kn/purchase-m.md`
- API 文档：`docs/api/endpoints/purchase-request-update-item.md`、`docs/api/endpoints/purchase-order-update-item.md`
