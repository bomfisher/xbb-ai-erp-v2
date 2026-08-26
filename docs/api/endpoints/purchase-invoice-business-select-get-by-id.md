# 采购发票业务选择回显

- 请求：`POST /erp/v1/purchase/purchaseInvoice/businessSelect/getById`
- 入参：`PurchaseInvoiceBusinessSelectQueryDTO`，必须提供 `corpid` 与 `id`。
- 返回：`PurchaseInvoiceBusinessSelectOptionVO`；找不到、未审核或未过账时返回空。

该接口用于编辑已有应付单时回显已选择的采购发票，即使该发票当前可开应付金额已用尽仍可回显。
