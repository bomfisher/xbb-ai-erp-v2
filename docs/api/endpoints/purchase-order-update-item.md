# 采购订单编辑表单

## 文档信息
- 领域：`purchase`
- 控制器：`PurchaseOrderAdminController#updateItem`
- 请求方式：`POST /erp/v1/purchase/purchaseOrder/updateItem`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1001
}
```

## 响应规则

响应为 `ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>>`。编辑场景返回 `UPDATE` 字段元数据、当前主档与 `items` 子档数据，并返回与新增场景相同的 `formSections`：`basic(10)`、`items(20)`、`amount(30)`、`remark(40)`。

前端按 `formSections[].order` 排序分组；`formSections` 缺失或为空时，继续使用历史 `headList` 属性前缀分组。该兼容规则保证其他业务以及旧版接口响应无需同步改造。

## 规则说明

- 编辑表单的字段、可编辑状态和子表列仍以本次响应的 `headList` 为准。
- `items` 子档按响应数据回显，新增、删除和编辑行为继续由 `SUB_ITEM(49)` 组件处理。
- 分组布局不是保存数据的一部分，正式保存仍提交 `main`、`items`、通用上下文和幂等号。
- `linkageConfig.itemStock` 指定产品行的 `skuId`、`warehouseId` 和 `currentStock` 字段；前端在选择产品或改变仓库时通过库存公共接口刷新当前库存数量。
- `linkageConfig.rowAmount` 指定产品行的 `qty × unitPrice` 计算结果写入临时 `amount` 字段；`linkageConfig.aggregateAmount` 将所有产品行金额合计回写到只读的 `main.totalAmount`。
