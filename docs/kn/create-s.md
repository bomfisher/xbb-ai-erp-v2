# create-s

## 客户
- 接口：`POST /erp/v1/customer/addItem`
- 用途：客户新增表单元数据初始化
- 差异点：返回 `headList + data + sectionState` 的新建抽屉结构
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-add-item.md`

## 供应商
- 接口：`POST /erp/v1/supplier/addItem`
- 用途：供应商新增表单初始化
- 差异点：返回主档、子档与 `sectionState` 的完整抽屉初始化结构
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-add-item.md`

## 采购
- 接口：`POST /erp/v1/purchase/request/addItem`、`POST /erp/v1/purchase/order/addItem`
- 用途：采购申请、采购订单新增表单初始化
- 差异点：产品明细以 `fieldType=50` 容器及 `subField` 下发，外层 `attr` 对齐 `data` 中的子档数组
- 业务文档：`docs/kn/purchase-m.md`
- API 文档：`docs/api/endpoints/purchase-request-add-item.md`、`docs/api/endpoints/purchase-order-add-item.md`
