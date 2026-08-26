# 采购发票业务选择快捷搜索

- 请求：`POST /erp/v1/purchase/purchaseInvoice/businessSelect/quickSearch`
- 入参：`PurchaseInvoiceBusinessSelectQueryDTO`，包含 `corpid`、可选 `keyword`。
- 返回：采购发票选择项列表，字段为 `id`、`code`、`name`、`label`。

仅返回当前租户已审核、已过账且可开应付金额大于零的采购发票。
