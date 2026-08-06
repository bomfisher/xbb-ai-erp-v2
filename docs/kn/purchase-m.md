# 采购

采购申请和采购订单的新建、编辑表单均通过 `headList + data` 下发。产品明细使用子档容器协议：`fieldType=50` 的外层字段以其 `attr` 对齐 `data` 中的数组，明细列位于 `subField`；SKU 选择列使用 `fieldType=12 + businessSelectConfig`。

- 采购申请新增：[purchase-request-add-item.md](../api/endpoints/purchase-request-add-item.md)
- 采购申请编辑：[purchase-request-update-item.md](../api/endpoints/purchase-request-update-item.md)
- 采购订单新增：[purchase-order-add-item.md](../api/endpoints/purchase-order-add-item.md)
- 采购订单编辑：[purchase-order-update-item.md](../api/endpoints/purchase-order-update-item.md)
- 字段协议：[field-product-user-m.md](field-product-user-m.md)
