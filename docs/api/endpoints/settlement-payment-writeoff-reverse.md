# 付款核销冲销

- 请求：`POST /erp/v1/settlement/paymentWriteoff/reverse`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅有效核销记录可冲销；冲销后恢复预付款与应付款余额并标记记录为已冲销，不自动生成退款或付款资金单。
- 响应：`ResultVO<BaseVO>`。
