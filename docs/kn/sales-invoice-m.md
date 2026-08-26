# 销售发票

销售发票过账是否自动生成应收开放项由系统配置 `SALES_INVOICE_AUTO_CREATE_RECEIVABLE` 决定，默认关闭；开启后，正常销售发票过账成功时在同一事务按当前可开余额创建应收开放项。关闭时不自动创建，用户可从同一张已过账发票手工拆分创建多张应收。

销售发票维护“已开应收金额”和“可开应收金额”；应收新增、编辑、删除与自动创建均按金额差额回写，已开金额不得超过发票含税金额。

## 单据动作

- 过账：`docs/api/endpoints/sales-invoice-post.md`
- 审核：`docs/api/endpoints/sales-invoice-audit.md`
- 反审核：`docs/api/endpoints/sales-invoice-unaudit.md`
- 作废：`docs/api/endpoints/sales-invoice-void.md`
- 红冲：`docs/api/endpoints/sales-invoice-red-flush.md`
- 红冲生成独立负数贷项发票、负数来源行关联和负数应收开放项；负数来源数量会冲回原单已开票数量，原发票保持可追溯且不会被物理删除。

## 开票来源

- 首期支持多张同类型的已审核销售出库单或销售订单带入发票明细，并允许追加手工附加项。
- 同一张发票不允许混用订单与出库两类正式来源；销售合同待合同模块及合同明细查询契约接入。
- 已占用开票数量按未作废发票的来源行数量实时汇总，包含已保存草稿；订单行汇总直接订单开票与关联出库开票，避免两种来源对同一订单行重复开票。
- 来源选择：`docs/api/endpoints/sales-invoice-source-documents.md`
- 来源明细预览：`docs/api/endpoints/sales-invoice-source-preview.md`

## 业务选择

### `businessSelect`

- 接口路径：`POST /erp/v1/sales/salesInvoice/businessSelect/quickSearch|dialogSearch|getById`
- 用途：为应收等下游业务提供销售发票选择与已有值回显。
- API：`docs/api/endpoints/sales-invoice-business-select-quick-search.md`
- API：`docs/api/endpoints/sales-invoice-business-select-dialog-search.md`
- API：`docs/api/endpoints/sales-invoice-business-select-get-by-id.md`
