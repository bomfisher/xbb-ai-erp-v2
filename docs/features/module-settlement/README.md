# 结算管理

本功能交付客户普通收款、预收和期初收款列表、新建、编辑、草稿及正式保存能力。普通收款在创建或编辑时可关联多笔同客户应收开放项，关联关系由 `receipt_writeoff` 保存；预收款不要求关联应收，保留后续核销能力。

- 模块目录：`xbb-erp-module-settlement`
- HTTP 前缀：`/erp/v1/settlement/receipt`
- 业务编码：`RECEIPT`
- 菜单：`settlement/receipt` → `settlement/receipt/list`

## 应收开放项

- HTTP 前缀：`/erp/v1/settlement/receivable`
- 业务编码：`RECEIVABLE`
- 菜单：`settlement/receivable` → `settlement/receivable/list`
- 数据表：`receivable`，迁移：`V20__create_receivable_table.sql`
- API 文档：`docs/api/endpoints/settlement-receivable.md`

## 供应商付款与预付款

- 付款单：`/erp/v1/settlement/payment/*`，菜单 `settlement/payment`。
- 预付款：`/erp/v1/settlement/advancePayment/*`，菜单 `settlement/advancePayment`，固定业务编码 `ADVANCE_PAYMENT`。
- 两个入口共用 `payment` 表；预付款余额用于付款核销。
