# 应付款红冲

- 请求：`POST /erp/v1/settlement/payable/redFlush`
- 入参：`corpid`、`userId`、`id`。
- 规则：仅未核销的有效应付款可红冲；系统关闭原单并生成一张已关闭的负数红冲凭据，来源采购发票的已开应付金额同步回退。
- 响应：`ResultVO<BaseVO>`。
