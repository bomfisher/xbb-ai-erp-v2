# 应收款作废

- 请求：`POST /erp/v1/settlement/receivable/void`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅未核销的有效应收款可作废；已核销或部分核销必须先逐笔冲销核销。来源为销售发票时同步回退该发票的已开应收金额。
- 响应：`ResultVO<BaseVO>`。
