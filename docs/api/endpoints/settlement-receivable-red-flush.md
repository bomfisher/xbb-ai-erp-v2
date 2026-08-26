# 应收款红冲

- 请求：`POST /erp/v1/settlement/receivable/redFlush`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅未核销的有效应收款可红冲；系统关闭原单并生成一张已关闭的负数红冲凭据，来源销售发票的已开应收金额同步回退。
- 响应：`ResultVO<BaseVO>`。
