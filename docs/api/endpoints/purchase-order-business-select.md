# 采购订单业务选择

采购订单以 `PURCHASE_ORDER` 作为业务选择编码，供入库单选择采购订单及其关联字段使用。

## 快捷搜索

- `POST /erp/v1/purchase/purchaseOrder/businessSelect/quickSearch`
- 入参：`corpid`、可选 `keyword`、可选 `supplierId`
- 返回：`ResultVO<List<PurchaseOrderBusinessSelectOptionVO>>`

## 弹窗搜索

- `POST /erp/v1/purchase/purchaseOrder/businessSelect/dialogSearch`
- 入参：`corpid`、可选 `keyword`、可选 `supplierId`、`pageNum`、`pageSize`
- 返回：`ResultVO<ListBaseVO<PurchaseOrderBusinessSelectOptionVO>>`

## 按 ID 回显

- `POST /erp/v1/purchase/purchaseOrder/businessSelect/getById`
- 入参：`corpid`、`id`
- 返回：订单不存在或不属于当前租户时 `data` 为 `null`。

候选项仅包含当前租户内至少有一行待入库数量的采购订单。传入 `supplierId` 时，进一步限制为该供应商的订单；未传时不限制供应商。候选项包含 `id`、`code`（采购订单号）、`name`（供应商名称）和 `label`；关键字匹配订单号或供应商名称。
