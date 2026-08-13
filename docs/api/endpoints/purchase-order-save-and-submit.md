# 采购订单正式保存

- 控制器：`PurchaseOrderAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/purchase/purchaseOrder/saveAndSubmit`
- 请求 DTO：`PurchaseOrderSubmitSaveDTO`
- 响应：`ResultVO<BaseVO>`

## 请求要点

除主档 `main` 与通用租户、操作人、草稿字段外，提交 `items` 采购产品数组。每行包含：

- `id`：编辑已有产品行时传入；新增时不传。
- `skuId`、`skuCode`、`skuName`、`specification`、`unitName`：产品及其订单快照。
- `warehouseId`、`currentStock`：采购业务页面使用的仓库与当前库存展示字段；当前采购订单行表未持久化这两个字段。
- `qty`、`unitPrice`、`taxRate`：数量、单价和税率。

正式保存要求至少一条产品行，且 SKU、SKU 编码、SKU 名称、单位、数量和单价不能为空；数量必须大于零，单价不能小于零。

## 保存规则

- 同一事务内先保存采购订单主档，再以主档 ID 新增、更新或逻辑删除产品行。
- 已有明细按 `id` 校验归属；重复 ID 或跨订单 ID 会被拒绝。
- 后端按 `qty × unitPrice` 重算行 `amount`，并汇总回写主档 `totalAmount`，不信任前端金额。
- 新增行的 `inboundQty` 初始化为零；更新时保留既有入库数量。
- 被本次请求移除且已存在入库数量的产品行不可删除。
- 正式保存全部成功后才删除指定草稿。
