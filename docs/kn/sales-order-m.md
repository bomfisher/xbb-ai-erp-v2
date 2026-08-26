# 销售订单

## 订单动作

销售订单新建时会提交冻结快照并匹配 `SALES_ORDER + CREATE` 的已启用流程。无匹配流程时订单标记为无需审批并立即预占库存；命中流程时订单与明细先以待审批状态落库，首节点完成并进入后续节点时更新为审批中，期间均不会预占库存或进入后续业务。审批通过后回调订单领域更新为已审批并预占库存；拒绝或撤回则更新为已拒绝且不执行库存副作用。普通订单列表默认仅展示无需审批和已审批订单，不展示审批状态列。

- 头部动作要求恰好选择一条订单。
- 销售出库通过销售出库新建页回填订单、客户和未出库明细，订单与客户不可修改。
- 审核：`docs/api/endpoints/sales-order-audit.md`
- 反审核：`docs/api/endpoints/sales-order-unaudit.md`
- 销售出库审核：[sales-outbound-audit.md](../api/endpoints/sales-outbound-audit.md)
- 销售出库反审核：[sales-outbound-unaudit.md](../api/endpoints/sales-outbound-unaudit.md)

销售订单开票状态为 `0 未开票`、`1 部分开票`、`2 全部开票`，按来源订单行的发票数量实时汇总，并在销售发票保存、作废、红冲或删除后更新。

## 子档即时库存

### `itemStock`

- 接口路径：`POST /erp/v1/sales/salesOrder/itemStock`
- 用途：产品选择后展示 SKU 即时库存；选择子档仓库后展示该仓库的即时库存。
- API：`docs/api/endpoints/sales-order-item-stock.md`

## 业务选择

### `businessSelect`
- 接口路径：`POST /erp/v1/sales/salesOrder/businessSelect/quickSearch|dialogSearch|getById`
- 用途：为销售出库等下游业务提供销售订单选择与已有值回显。
- API：`docs/api/endpoints/sales-order-business-select-quick-search.md`
- API：`docs/api/endpoints/sales-order-business-select-dialog-search.md`
- API：`docs/api/endpoints/sales-order-business-select-get-by-id.md`
