# 结算管理

## 付款单

- 接口基础路径：`POST /erp/v1/settlement/payment/*`
- 能力：仅查询和维护供应商付款；类型由后端固定为 `SUPPLIER_PAYMENT`，不在表单展示。
- API 文档：`docs/api/endpoints/settlement-payment.md`

## 预付款

- 接口基础路径：`POST /erp/v1/settlement/advancePayment/*`
- 能力：仅查询和维护预付款；类型由后端固定为 `ADVANCE_PAYMENT`，不在表单展示。
- API 文档：`docs/api/endpoints/settlement-advance-payment.md`

## 客户收款

- 接口基础路径：`POST /erp/v1/settlement/receipt/*`
- 能力：列表、动态表单、新建编号、草稿、正式保存；普通收款在保存时可同时核销多笔应收，预收款保留后续核销能力。
- API 文档：`docs/api/endpoints/settlement-receipt.md`

## 预收款

- 接口基础路径：`POST /erp/v1/settlement/advanceReceipt/*`
- 能力：仅查询和新建 `ADVANCE_PAYMENT` 类型的收款记录，编号独立于普通收款。
- API 文档：`docs/api/endpoints/settlement-advance-receipt.md`

## 应收开放项

- 接口基础路径：`POST /erp/v1/settlement/receivable/*`
- 能力：列表、动态表单、新建编号、草稿、正式保存，以及供收款单选择的按客户未结清应收查询。
- API 文档：`docs/api/endpoints/settlement-receivable.md`
- 作废：`docs/api/endpoints/settlement-receivable-void.md`
- 红冲：`docs/api/endpoints/settlement-receivable-red-flush.md`

## 应付款与核销冲销

- 应付款作废与红冲：`docs/api/endpoints/settlement-payable-void.md`、`docs/api/endpoints/settlement-payable-red-flush.md`
- 手动创建付款核销：`docs/api/endpoints/settlement-payment-writeoff-save-and-submit.md`
- 查询付款核销预付款来源：`docs/api/endpoints/settlement-payment-writeoff-advance-sources.md`
- 查询付款核销应付款来源：`docs/api/endpoints/settlement-payment-writeoff-payable-sources.md`
- 收款核销冲销：`docs/api/endpoints/settlement-receipt-writeoff-reverse.md`
- 付款核销冲销：`docs/api/endpoints/settlement-payment-writeoff-reverse.md`
- 规则：发生核销时先冲销核销，再作废或红冲开放项；冲销核销仅恢复余额，真实退款、收回款需从独立资金单入口办理。
