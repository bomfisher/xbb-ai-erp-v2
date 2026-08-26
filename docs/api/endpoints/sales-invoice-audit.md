# 销售发票审核接口

接口：`POST /erp/v1/sales/salesInvoice/audit`

请求使用 `IdBaseDTO`，必须携带 `corpid`、`userId` 和 `id`。仅待审核或已拒绝的销售发票允许审核；成功后审核状态变为 `APPROVED` 并记录审核时间。

返回：`ResultVO<BaseVO>`。
