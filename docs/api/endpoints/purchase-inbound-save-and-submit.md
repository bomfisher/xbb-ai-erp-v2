# 采购入库单正式保存

- 控制器：`PurchaseInboundAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/purchase/purchaseInbound/saveAndSubmit`
- 请求 DTO：`PurchaseInboundSubmitSaveDTO`
- 响应：`ResultVO<BaseVO>`

## 请求要点

除主档 `main` 与通用租户、操作人、草稿字段外，提交 `items` 入库产品数组。每行包含 `id`、`purchaseOrderItemId`、`skuId`、`skuName`、`unitName`、`qty`、`unitPrice` 和可选 `costUnit`。

正式保存至少需要一行产品；来源采购订单行、SKU、SKU 名称、单位、数量和采购单价不能为空。数量必须大于零，采购单价和成本单价不能小于零。

## 保存规则

- 同一事务内先保存入库主档，再以主档 ID 新增、更新或逻辑删除入库产品行。
- 已有明细按 `id` 校验必须归属当前入库单；重复或跨单据 ID 会被拒绝。
- 后端重算产品行 `amount = qty × unitPrice`、`costAmount = qty × costUnit`，并重算主档 `totalAmount`。
- 未提交 `costUnit` 时，按 `unitPrice` 初始化成本单价。
- 草稿携带 `items`，正式保存全部成功后才删除来源草稿。
- 已确认入库（`INVENTORY_POSTED`）的单据不可再次提交修改，必须走后续的冲销或退货业务。
- 审批策略判定免审时，提交保存会在同一事务内自动执行确认入库；需要审批时仅保存为 `SUBMITTED`，由审核通过回调调用确认入库接口。
