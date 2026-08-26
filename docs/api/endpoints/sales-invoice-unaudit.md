# 销售发票反审核接口

接口：`POST /erp/v1/sales/salesInvoice/unaudit`

请求使用 `IdBaseDTO`，必须携带 `corpid`、`userId` 和 `id`。仅已审核的销售发票允许反审核；成功后审核状态恢复为 `PENDING` 并清空审核时间。

返回：`ResultVO<BaseVO>`。
