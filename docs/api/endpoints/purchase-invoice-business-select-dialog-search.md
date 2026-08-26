# 采购发票业务选择弹窗查询

- 请求：`POST /erp/v1/purchase/purchaseInvoice/businessSelect/dialogSearch`
- 入参：`PurchaseInvoiceBusinessSelectQueryDTO`，包含 `corpid`、可选 `keyword`、`pageNum`、`pageSize`。
- 返回：`ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO>`。

结果仅包含当前租户已审核、已过账且可开应付金额大于零的采购发票。
