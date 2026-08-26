# 应收销售发票选择回填

- 请求：`POST /erp/v1/settlement/receivable/selectionFill`
- 入参：`SettlementSelectionFillDTO`，包含 `corpid`、`fieldAttr=main.sourceInvoiceId` 与销售发票 `referenceId`。
- 返回：`SettlementSelectionFillVO`，在 `patch` 中回填 `main.sourceInvoiceId` 与 `main.customerId`。

来源销售发票必须为当前租户已审核、已过账的有效发票。前端先选择客户时，以 `customerId` 作为销售发票选择条件；选择销售发票时自动回填客户，修改或清空客户会清空已选销售发票。
