# 收款核销冲销

- 请求：`POST /erp/v1/settlement/receiptWriteoff/reverse`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅有效核销记录可冲销；冲销后恢复预收款与应收款余额并标记记录为已冲销，不自动生成退款或收款资金单。
- 响应：`ResultVO<BaseVO>`。
