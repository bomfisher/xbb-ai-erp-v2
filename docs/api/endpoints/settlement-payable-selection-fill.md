# 应付采购发票选择回填

- 请求：`POST /erp/v1/settlement/payable/selectionFill`
- 入参：`SettlementSelectionFillDTO`，包含 `corpid`、`fieldAttr=main.sourceInvoiceId` 与采购发票 `referenceId`。
- 返回：`SettlementSelectionFillVO`，在 `patch` 中回填 `main.sourceInvoiceId`、`main.supplierId` 与采购发票的可开应付金额 `main.amount`。

来源采购发票必须为当前租户已审核、已过账的有效发票。前端先选择供应商时，以 `supplierId` 作为采购发票选择条件；选择采购发票时自动回填供应商，修改或清空供应商会清空已选采购发票。
