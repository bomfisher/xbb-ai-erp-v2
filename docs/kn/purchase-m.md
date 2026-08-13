# 采购

采购申请和采购订单的新建、编辑表单均通过 `headList + data` 下发。采购合同以 `items` 子档维护产品行，行内产品使用专用 `PRODUCT(50) + productSelectConfig`，仓库使用 `BUSINESS(16)`，库存、单位、数量和单价由采购合同自身字段元数据定义；各业务自行决定产品字段集合与表单渲染，不在产品选择器中硬编码。正式保存时主档与产品行在同一事务内同步，服务端重算订单总额和产品行金额。

采购入库单同样使用 `items` 子档维护入库产品行，行内产品使用专用 `PRODUCT(50) + productSelectConfig`；正式保存时主档与入库产品行同步，并由后端重算采购金额、成本金额和主档总额。

采购入库单提交后由审批策略决定是否自动确认：免审时在提交事务内确认入库；需审时由审核通过调用统一确认入口。确认入库从已持久化的明细读取数量和成本，调用库存 contract 记账，成功后状态变为 `INVENTORY_POSTED`。

- 采购申请新增：[purchase-request-add-item.md](../api/endpoints/purchase-request-add-item.md)
- 采购申请编辑：[purchase-request-update-item.md](../api/endpoints/purchase-request-update-item.md)
- 采购订单新增：[purchase-order-add-item.md](../api/endpoints/purchase-order-add-item.md)
- 采购订单编辑：[purchase-order-update-item.md](../api/endpoints/purchase-order-update-item.md)
- 采购订单业务选择：[purchase-order-business-select.md](../api/endpoints/purchase-order-business-select.md)
- 采购入库正式保存：[purchase-inbound-save-and-submit.md](../api/endpoints/purchase-inbound-save-and-submit.md)
- 采购入库确认：[purchase-inbound-confirm-inbound.md](../api/endpoints/purchase-inbound-confirm-inbound.md)
- 字段协议：[field-product-user-m.md](field-product-user-m.md)
