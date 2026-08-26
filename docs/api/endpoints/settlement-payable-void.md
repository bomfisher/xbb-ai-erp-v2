# 应付款作废

- 请求：`POST /erp/v1/settlement/payable/void`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅未核销的有效应付款可作废；已核销或部分核销必须先逐笔冲销核销。来源为采购发票时同步回退该发票的已开应付金额。
- 响应：`ResultVO<BaseVO>`。
